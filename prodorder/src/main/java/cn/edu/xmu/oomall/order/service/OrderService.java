//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.IdempotencyAcquireResult;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderIdempotentDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderCreateMessage;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import cn.edu.xmu.oomall.order.service.exception.OrderCreationInProgressException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${oomall.order.server-num}")
    private int serverNum;

    private final GoodsDao goodsDao;

    private final OrderDao orderDao;

    private final OrderIdempotentDao orderIdempotentDao;

    private final RocketMQTemplate rocketMQTemplate;

    @Autowired
    public OrderService(GoodsDao goodsDao, OrderDao orderDao, OrderIdempotentDao orderIdempotentDao, RocketMQTemplate rocketMQTemplate) {
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
        this.orderIdempotentDao = orderIdempotentDao;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<Long, List<OrderItem>> packOrder(List<OrderItemDto> items, UserDto customer) {
        Map<Long, List<OrderItem>> packs = new HashMap<>();
        for (OrderItemDto item : items) {
            OnsaleDto onsaleDto = fetchOnsale(item.getOnsaleId());
            OrderItem orderItem = buildOrderItem(item, customer, onsaleDto);
            packs.computeIfAbsent(onsaleDto.getShop().getId(), shopId -> new ArrayList<>()).add(orderItem);
        }
        return packs;
    }

    private OnsaleDto fetchOnsale(Long onsaleId) {
        InternalReturnObject<OnsaleDto> onsaleRet = goodsDao.getOnsaleById(PLATFORM, onsaleId);
        if (onsaleRet == null || onsaleRet.getData() == null) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "销售", onsaleId));
        }

        OnsaleDto onsaleDto = onsaleRet.getData();
        if (onsaleDto.getShop() == null || onsaleDto.getProduct() == null) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,
                    String.format("销售(id=%d)数据不完整", onsaleId));
        }
        return onsaleDto;
    }

    private OrderItem buildOrderItem(OrderItemDto item, UserDto customer, OnsaleDto onsaleDto) {
        int maxQuantity = onsaleDto.getMaxQuantity() != null ? onsaleDto.getMaxQuantity() : Integer.MAX_VALUE;
        if (item.getQuantity() > maxQuantity) {
            throw new BusinessException(ReturnNo.ITEM_OVERMAXQUANTITY,
                    String.format(ReturnNo.ITEM_OVERMAXQUANTITY.getMessage(), onsaleDto.getId(), item.getQuantity(), maxQuantity));
        }

        OrderItem orderItem = OrderItem.builder()
                .onsaleId(onsaleDto.getId())
                .price(onsaleDto.getPrice())
                .name(onsaleDto.getProduct().getName())
                .quantity(item.getQuantity())
                .creatorId(customer.getId())
                .creatorName(customer.getName())
                .build();

        if (item.getActId() != null && onsaleDto.getActList() != null
                && onsaleDto.getActList().stream().anyMatch(activity -> Objects.equals(activity.getId(), item.getActId()))) {
            orderItem.setActId(item.getActId());
            // TODO: 需要查看优惠券 id 所属的活动是否在 onsale 的活动列表中，并且优惠券是有效的，才能设置到 orderItem 中
        }
        if (item.getCouponId() != null) {
            orderItem.setCouponId(item.getCouponId());
        }
        return orderItem;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(String idempotentKey, Map<Long, List<OrderItem>> packs, ConsigneeDto consignee, String message, UserDto customer) {
        if (idempotentKey == null || idempotentKey.isBlank()) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "订单幂等键不能为空");
        }
        if (packs == null || packs.isEmpty()) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "订单明细不能为空");
        }

        IdempotencyAcquireResult acquireResult = orderIdempotentDao.tryAcquire(idempotentKey, packs.size());
        switch (acquireResult) {
            case ALREADY_COMMITTED -> {
                logger.info("订单已创建，跳过重复处理，idempotentKey={}", idempotentKey);
                return;
            }
            case IN_PROGRESS -> throw new OrderCreationInProgressException(idempotentKey);
            case FAILED -> throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,
                    String.format("订单创建已失败，idempotentKey=%s", idempotentKey));
            default -> {
            }
        }

        LocalDateTime now = LocalDateTime.now();
        for (Map.Entry<Long, List<OrderItem>> entry : packs.entrySet()) {
            Order order = Order.builder()
                    .creatorId(customer.getId())
                    .customerId(customer.getId())
                    .creatorName(customer.getName())
                    .gmtCreate(now)
                    .gmtModified(now)
                    .shopId(entry.getKey())
                    .consignee(consignee.getConsignee())
                    .address(consignee.getAddress())
                    .mobile(consignee.getMobile())
                    .regionId(consignee.getRegionId())
                    .orderSn(Common.genSeqNum(serverNum))
                    .idempotentKey(idempotentKey)
                    .message(message)
                    .orderItems(entry.getValue())
                    .build();
            orderDao.createOrder(order);
        }
        orderIdempotentDao.markCommitted(idempotentKey);
    }

    public RocketMQLocalTransactionState resolveTransactionState(OrderCreateMessage orderCreateMessage) {
        if (orderCreateMessage == null || orderCreateMessage.getIdempotentKey() == null
                || orderCreateMessage.getIdempotentKey().isBlank()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return orderIdempotentDao.resolveTransactionState(orderCreateMessage.getIdempotentKey());
    }

    public void createOrder(List<OrderItemDto> items, ConsigneeDto consignee, String message, UserDto customer) {
        Map<Long, List<OrderItem>> packs = packOrder(items, customer);
        OrderCreateMessage orderCreateMessage = OrderCreateMessage.builder()
                .idempotentKey(UUID.randomUUID().toString())
                .packs(packs)
                .consignee(consignee)
                .message(message)
                .user(customer)
                .build();
        String payload = JacksonUtil.toJson(orderCreateMessage);
        Message<String> msg = MessageBuilder.withPayload(payload).build();
        rocketMQTemplate.sendMessageInTransaction("order-topic:1", msg, null);
    }
}
