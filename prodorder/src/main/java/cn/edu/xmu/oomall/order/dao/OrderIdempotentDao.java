//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.oomall.order.dao.bo.OrderIdempotentStatus;
import cn.edu.xmu.oomall.order.mapper.OrderIdempotentPoMapper;
import cn.edu.xmu.oomall.order.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.po.OrderIdempotentPo;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class OrderIdempotentDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderIdempotentDao.class);

    private final OrderIdempotentPoMapper orderIdempotentPoMapper;

    private final OrderPoMapper orderPoMapper;

    @Autowired
    public OrderIdempotentDao(OrderIdempotentPoMapper orderIdempotentPoMapper, OrderPoMapper orderPoMapper) {
        this.orderIdempotentPoMapper = orderIdempotentPoMapper;
        this.orderPoMapper = orderPoMapper;
    }

    /**
     * 独立事务提前写入 PENDING，避免本地事务提交前回查误判 ROLLBACK。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void ensurePending(String idempotentKey, int expectedShopCount) {
        if (orderIdempotentPoMapper.findById(idempotentKey).isPresent()) {
            return;
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            OrderIdempotentPo record = OrderIdempotentPo.builder()
                    .idempotentKey(idempotentKey)
                    .status(OrderIdempotentStatus.PENDING)
                    .expectedShopCount(expectedShopCount)
                    .gmtCreate(now)
                    .gmtModified(now)
                    .build();
            orderIdempotentPoMapper.saveAndFlush(record);
        } catch (DataIntegrityViolationException ex) {
            if (!isDuplicateKeyException(ex)) {
                throw ex;
            }
        }
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    public OrderIdempotentPo lockRecord(String idempotentKey) {
        return orderIdempotentPoMapper.findByIdForUpdate(idempotentKey)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("幂等记录不存在，idempotentKey=%s", idempotentKey)));
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    public void markCommitted(String idempotentKey) {
        OrderIdempotentPo record = lockRecord(idempotentKey);
        record.setStatus(OrderIdempotentStatus.COMMITTED);
        record.setGmtModified(LocalDateTime.now());
        orderIdempotentPoMapper.save(record);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void markFailed(String idempotentKey) {
        Optional<OrderIdempotentPo> optionalRecord = orderIdempotentPoMapper.findById(idempotentKey);
        if (optionalRecord.isEmpty()) {
            return;
        }
        OrderIdempotentPo record = optionalRecord.get();
        if (OrderIdempotentStatus.COMMITTED == record.getStatus()) {
            return;
        }
        record.setStatus(OrderIdempotentStatus.FAILED);
        record.setGmtModified(LocalDateTime.now());
        orderIdempotentPoMapper.save(record);
    }

    public RocketMQLocalTransactionState resolveTransactionState(String idempotentKey) {
        Optional<OrderIdempotentPo> optionalRecord = orderIdempotentPoMapper.findById(idempotentKey);
        if (optionalRecord.isEmpty()) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        OrderIdempotentPo record = optionalRecord.get();
        if (OrderIdempotentStatus.FAILED == record.getStatus()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        long actualCount = orderPoMapper.countByIdempotentKey(idempotentKey);
        if (actualCount >= record.getExpectedShopCount()) {
            if (OrderIdempotentStatus.PENDING == record.getStatus()) {
                logger.warn("幂等记录仍为进行中但订单已全部落库，视为成功，idempotentKey={}", idempotentKey);
            }
            return RocketMQLocalTransactionState.COMMIT;
        }

        if (OrderIdempotentStatus.COMMITTED == record.getStatus()) {
            logger.warn("幂等记录已成功但店铺订单数不足，idempotentKey={}，expected={}，actual={}",
                    idempotentKey, record.getExpectedShopCount(), actualCount);
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    private boolean isDuplicateKeyException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return true;
            }
            String message = cause.getMessage();
            if (message != null && (message.contains("Duplicate entry") || message.contains("duplicate key"))) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
