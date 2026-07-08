//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.oomall.order.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.po.OrderItemPo;
import cn.edu.xmu.oomall.order.mapper.po.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Repository
public class OrderDao {

    private final OrderPoMapper orderPoMapper;

    private final OrderItemPoMapper orderItemPoMapper;

    @Autowired
    public OrderDao(OrderPoMapper orderPoMapper, OrderItemPoMapper orderItemPoMapper) {
        this.orderPoMapper = orderPoMapper;
        this.orderItemPoMapper = orderItemPoMapper;
    }

    public long countByIdempotentKey(String idempotentKey) {
        return idempotentKey == null ? 0L : orderPoMapper.countByIdempotentKey(idempotentKey);
    }

    public Set<Long> findShopIdsByIdempotentKey(String idempotentKey) {
        if (idempotentKey == null) {
            return Collections.emptySet();
        }
        return orderPoMapper.findShopIdsByIdempotentKey(idempotentKey);
    }

    public boolean existsByIdempotentKeyAndShopId(String idempotentKey, Long shopId) {
        return idempotentKey != null && shopId != null
                && orderPoMapper.existsByIdempotentKeyAndShopId(idempotentKey, shopId);
    }

    /**
     * 幂等创建店铺订单。已存在时返回 null；新建成功返回订单 ID。
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createOrderIfAbsent(Order order) {
        if (existsByIdempotentKeyAndShopId(order.getIdempotentKey(), order.getShopId())) {
            return null;
        }
        try {
            return createOrder(order);
        } catch (DataIntegrityViolationException ex) {
            if (isDuplicateKeyException(ex)) {
                return null;
            }
            throw ex;
        }
    }

    private Long createOrder(Order order) {
        LocalDateTime now = order.getGmtCreate() != null ? order.getGmtCreate() : LocalDateTime.now();
        OrderPo orderPo = OrderPo.builder()
                .creatorId(order.getCreatorId())
                .creatorName(order.getCreatorName())
                .customerId(order.getCustomerId())
                .shopId(order.getShopId())
                .orderSn(order.getOrderSn())
                .consignee(order.getConsignee())
                .regionId(order.getRegionId())
                .address(order.getAddress())
                .mobile(order.getMobile())
                .message(order.getMessage())
                .idempotentKey(order.getIdempotentKey())
                .gmtCreate(now)
                .gmtModified(order.getGmtModified() != null ? order.getGmtModified() : now)
                .build();
        orderPo = orderPoMapper.saveAndFlush(orderPo);

        for (OrderItem orderItem : order.getOrderItems()) {
            // 库存扣减在订单事务提交后由 NewOrderPublisher 通知 goods-service 处理
            OrderItemPo orderItemPo = OrderItemPo.builder()
                    .orderId(orderPo.getId())
                    .creatorId(orderItem.getCreatorId())
                    .creatorName(orderItem.getCreatorName())
                    .onsaleId(orderItem.getOnsaleId())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getPrice())
                    .name(orderItem.getName())
                    .couponActivityId(orderItem.getActId())
                    .couponId(orderItem.getCouponId())
                    .commented((byte) 0)
                    .gmtCreate(now)
                    .gmtModified(now)
                    .build();
            orderItemPoMapper.save(orderItemPo);
        }
        return orderPo.getId();
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
