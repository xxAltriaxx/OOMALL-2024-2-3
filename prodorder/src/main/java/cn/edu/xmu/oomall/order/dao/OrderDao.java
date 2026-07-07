//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.oomall.order.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.po.OrderItemPo;
import cn.edu.xmu.oomall.order.mapper.po.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) {
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
        orderPo = orderPoMapper.save(orderPo);

        for (OrderItem orderItem : order.getOrderItems()) {
            // TODO: 先要减去货品数量
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
    }
}
