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

    public IdempotencyAcquireResult tryAcquire(String idempotentKey, int expectedShopCount) {
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
            return IdempotencyAcquireResult.ACQUIRED;
        } catch (DataIntegrityViolationException ex) {
            if (!isDuplicateKeyException(ex)) {
                throw ex;
            }
            return findByIdempotentKey(idempotentKey)
                    .map(this::toAcquireResult)
                    .orElseThrow(() -> ex);
        }
    }

    public void markCommitted(String idempotentKey) {
        OrderIdempotentPo record = findByIdempotentKey(idempotentKey)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("幂等记录不存在，idempotentKey=%s", idempotentKey)));
        record.setStatus(OrderIdempotentStatus.COMMITTED);
        record.setGmtModified(LocalDateTime.now());
        orderIdempotentPoMapper.save(record);
    }

    public RocketMQLocalTransactionState resolveTransactionState(String idempotentKey) {
        Optional<OrderIdempotentPo> optionalRecord = findByIdempotentKey(idempotentKey);
        if (optionalRecord.isEmpty()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        OrderIdempotentPo record = optionalRecord.get();
        if (OrderIdempotentStatus.PENDING == record.getStatus()) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        if (OrderIdempotentStatus.FAILED == record.getStatus()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        long actualCount = orderPoMapper.countByIdempotentKey(idempotentKey);
        if (actualCount >= record.getExpectedShopCount()) {
            return RocketMQLocalTransactionState.COMMIT;
        }

        logger.warn("幂等记录已成功但店铺订单数不足，idempotentKey={}，expected={}，actual={}",
                idempotentKey, record.getExpectedShopCount(), actualCount);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    private Optional<OrderIdempotentPo> findByIdempotentKey(String idempotentKey) {
        return orderIdempotentPoMapper.findById(idempotentKey);
    }

    private IdempotencyAcquireResult toAcquireResult(OrderIdempotentPo record) {
        if (OrderIdempotentStatus.COMMITTED == record.getStatus()) {
            return IdempotencyAcquireResult.ALREADY_COMMITTED;
        }
        if (OrderIdempotentStatus.PENDING == record.getStatus()) {
            return IdempotencyAcquireResult.IN_PROGRESS;
        }
        return IdempotencyAcquireResult.FAILED;
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
