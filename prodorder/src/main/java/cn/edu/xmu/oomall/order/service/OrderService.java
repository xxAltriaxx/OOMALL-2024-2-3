//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderIdempotentDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderIdempotentStatus;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.PromotionDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponActDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import cn.edu.xmu.oomall.order.mapper.po.OrderIdempotentPo;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderCreateMessage;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import cn.edu.xmu.oomall.order.util.InternalReturnObjectHelper;
import cn.edu.xmu.oomall.order.util.OrderIdempotentKeyGenerator;
import cn.edu.xmu.oomall.order.util.OrderMessageConverter;
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
import java.util.Set;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private static final byte COUPON_ACT_ACTIVE = 1;

    private static final byte COUPON_UNUSED = 0;

    @Value("${oomall.order.server-num}")
    private int serverNum;

    private final GoodsDao goodsDao;

    private final PromotionDao promotionDao;

    private final OrderDao orderDao;

    private final OrderIdempotentDao orderIdempotentDao;

    private final NewOrderPublisher newOrderPublisher;

    private final RocketMQTemplate rocketMQTemplate;

    @Autowired
    public OrderService(GoodsDao goodsDao, PromotionDao promotionDao, OrderDao orderDao,
                        OrderIdempotentDao orderIdempotentDao, NewOrderPublisher newOrderPublisher,
                        RocketMQTemplate rocketMQTemplate) {
        this.goodsDao = goodsDao;
        this.promotionDao = promotionDao;
        this.orderDao = orderDao;
        this.orderIdempotentDao = orderIdempotentDao;
        this.newOrderPublisher = newOrderPublisher;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public void prepareForTransaction(String idempotentKey, int expectedShopCount) {
        orderIdempotentDao.ensurePending(idempotentKey, expectedShopCount);
    }

    public void failOrderTransaction(String idempotentKey) {
        orderIdempotentDao.markFailed(idempotentKey);
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
        OnsaleDto onsaleDto = InternalReturnObjectHelper.requireData(
                goodsDao.getOnsaleById(PLATFORM, onsaleId),
                ReturnNo.RESOURCE_ID_NOTEXIST, "销售", onsaleId);
        if (onsaleDto.getShop() == null || onsaleDto.getProduct() == null) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,
                    String.format("销售(id=%d)数据不完整", onsaleId));
        }
        validateOnsaleEffective(onsaleDto);
        validateStock(onsaleDto, item.getQuantity());
        return onsaleDto;
    }

    private void validateStock(OnsaleDto onsaleDto, int requestedQuantity) {
        if (onsaleDto.getQuantity() == null || onsaleDto.getQuantity() < requestedQuantity) {
            Long productId = onsaleDto.getProduct() != null ? onsaleDto.getProduct().getId() : onsaleDto.getId();
            throw new BusinessException(ReturnNo.GOODS_STOCK_SHORTAGE,
                    String.format(ReturnNo.GOODS_STOCK_SHORTAGE.getMessage(), productId));
        }
    }

    private void validateOnsaleEffective(OnsaleDto onsaleDto) {
        LocalDateTime now = LocalDateTime.now();
        if ((onsaleDto.getBeginTime() != null && now.isBefore(onsaleDto.getBeginTime()))
                || (onsaleDto.getEndTime() != null && now.isAfter(onsaleDto.getEndTime()))) {
            throw new BusinessException(ReturnNo.GOODS_ONSALE_NOTEFFECTIVE,
                    String.format(ReturnNo.GOODS_ONSALE_NOTEFFECTIVE.getMessage(), onsaleDto.getId()));
        }
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

        applyCouponIfValid(item, orderItem, onsaleDto, customer);
        return orderItem;
    }

    private void applyCouponIfValid(OrderItemDto item, OrderItem orderItem, OnsaleDto onsaleDto, UserDto customer) {
        if (item.getCouponId() == null) {
            if (item.getActId() != null) {
                validateActOnOnsale(item.getActId(), onsaleDto);
                validateCouponAct(item.getActId());
                orderItem.setActId(item.getActId());
            }
            return;
        }

        if (item.getActId() == null) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "使用优惠券时必须指定优惠活动");
        }

        validateActOnOnsale(item.getActId(), onsaleDto);
        validateCouponAct(item.getActId());
        validateCustomerCoupon(item.getCouponId(), item.getActId(), customer.getId());

        orderItem.setActId(item.getActId());
        orderItem.setCouponId(item.getCouponId());
    }

    private void validateActOnOnsale(Long actId, OnsaleDto onsaleDto) {
        if (onsaleDto.getActList() == null
                || onsaleDto.getActList().stream().noneMatch(activity -> Objects.equals(activity.getId(), actId))) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID,
                    String.format("销售(id=%d)不支持优惠活动(id=%d)", onsaleDto.getId(), actId));
        }
    }

    private void validateCouponAct(Long actId) {
        CouponActDto couponAct = InternalReturnObjectHelper.requireData(
                goodsDao.getCouponActById(actId),
                ReturnNo.RESOURCE_ID_NOTEXIST, "优惠活动", actId);
        if (couponAct.getStatus() == null || COUPON_ACT_ACTIVE != couponAct.getStatus()) {
            throw new BusinessException(ReturnNo.COUPON_END,
                    String.format("优惠活动(id=%d)未生效或已终止", actId));
        }
        LocalDateTime now = LocalDateTime.now();
        if (couponAct.getBeginTime() != null && now.isBefore(couponAct.getBeginTime())) {
            throw new BusinessException(ReturnNo.COUPON_NOTBEGIN, ReturnNo.COUPON_NOTBEGIN.getMessage());
        }
        if (couponAct.getEndTime() != null && now.isAfter(couponAct.getEndTime())) {
            throw new BusinessException(ReturnNo.COUPON_END, ReturnNo.COUPON_END.getMessage());
        }
    }

    private void validateCustomerCoupon(Long couponId, Long actId, Long customerId) {
        CouponDto coupon = InternalReturnObjectHelper.requireData(
                promotionDao.getCouponById(customerId, couponId),
                ReturnNo.RESOURCE_ID_NOTEXIST, "优惠券", couponId);
        if (!Objects.equals(coupon.getCustomerId(), customerId)) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, "优惠券不属于当前用户");
        }
        if (!Objects.equals(coupon.getActivityId(), actId)) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID,
                    String.format("优惠券(id=%d)不属于优惠活动(id=%d)", couponId, actId));
        }
        if (coupon.getStatus() != null && COUPON_UNUSED != coupon.getStatus()) {
            throw new BusinessException(ReturnNo.COUPON_EXIST, ReturnNo.COUPON_EXIST.getMessage());
        }
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidBegin() != null && now.isBefore(coupon.getValidBegin())) {
            throw new BusinessException(ReturnNo.COUPON_NOTBEGIN, ReturnNo.COUPON_NOTBEGIN.getMessage());
        }
        if (coupon.getValidEnd() != null && now.isAfter(coupon.getValidEnd())) {
            throw new BusinessException(ReturnNo.COUPON_END, ReturnNo.COUPON_END.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(String idempotentKey, Map<Long, List<OrderItem>> packs, ConsigneeDto consignee, String message, UserDto customer) {
        if (idempotentKey == null || idempotentKey.isBlank()) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "订单幂等键不能为空");
        }
        if (packs == null || packs.isEmpty()) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "订单明细不能为空");
        }

        orderIdempotentDao.ensurePending(idempotentKey, packs.size());
        OrderIdempotentPo record = orderIdempotentDao.lockRecord(idempotentKey);
        if (OrderIdempotentStatus.FAILED == record.getStatus()) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,
                    String.format("订单创建已失败，idempotentKey=%s", idempotentKey));
        }

        long existingCount = orderDao.countByIdempotentKey(idempotentKey);
        if (OrderIdempotentStatus.COMMITTED == record.getStatus()) {
            if (existingCount >= record.getExpectedShopCount()) {
                logger.info("订单已创建，跳过重复处理，idempotentKey={}", idempotentKey);
                return;
            }
            logger.warn("幂等记录已成功但店铺订单不足，尝试补建，idempotentKey={}，expected={}，actual={}",
                    idempotentKey, record.getExpectedShopCount(), existingCount);
        }

        Set<Long> existingShopIds = orderDao.findShopIdsByIdempotentKey(idempotentKey);
        LocalDateTime now = LocalDateTime.now();
        List<OrderItem> itemsForStockDeduction = new ArrayList<>();
        Long firstCreatedOrderId = null;
        for (Map.Entry<Long, List<OrderItem>> entry : packs.entrySet()) {
            if (existingShopIds.contains(entry.getKey())) {
                continue;
            }
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
            Long createdOrderId = orderDao.createOrderIfAbsent(order);
            if (createdOrderId != null) {
                if (firstCreatedOrderId == null) {
                    firstCreatedOrderId = createdOrderId;
                }
                itemsForStockDeduction.addAll(entry.getValue());
            }
        }

        long actualCount = orderDao.countByIdempotentKey(idempotentKey);
        if (actualCount < packs.size()) {
            throw new IllegalStateException(String.format(
                    "店铺订单未全部创建，idempotentKey=%s，expected=%d，actual=%d",
                    idempotentKey, packs.size(), actualCount));
        }
        orderIdempotentDao.markCommitted(idempotentKey);
        if (!itemsForStockDeduction.isEmpty()) {
            newOrderPublisher.publishAfterCommit(firstCreatedOrderId, itemsForStockDeduction);
        }
    }

    public RocketMQLocalTransactionState resolveTransactionState(OrderCreateMessage orderCreateMessage) {
        if (orderCreateMessage == null || orderCreateMessage.getIdempotentKey() == null
                || orderCreateMessage.getIdempotentKey().isBlank()) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return orderIdempotentDao.resolveTransactionState(orderCreateMessage.getIdempotentKey());
    }

    public void createOrder(List<OrderItemDto> items, ConsigneeDto consignee, String message, UserDto customer,
                            String clientIdempotentKey) {
        Map<Long, List<OrderItem>> packs = packOrder(items, customer);
        String idempotentKey = resolveIdempotentKey(clientIdempotentKey, customer.getId(), items, consignee, message);
        OrderCreateMessage orderCreateMessage = OrderCreateMessage.builder()
                .idempotentKey(idempotentKey)
                .packs(OrderMessageConverter.fromPacks(packs))
                .consignee(consignee)
                .message(message)
                .user(customer)
                .build();
        String payload = JacksonUtil.toJson(orderCreateMessage);
        Message<String> msg = MessageBuilder.withPayload(payload).build();
        rocketMQTemplate.sendMessageInTransaction("order-topic:1", msg, null);
    }

    private String resolveIdempotentKey(String clientIdempotentKey, Long customerId, List<OrderItemDto> items,
                                        ConsigneeDto consignee, String message) {
        if (clientIdempotentKey != null && !clientIdempotentKey.isBlank()) {
            if (clientIdempotentKey.length() > 64) {
                throw new BusinessException(ReturnNo.FIELD_NOTVALID, "幂等键长度不能超过64");
            }
            return clientIdempotentKey;
        }
        return OrderIdempotentKeyGenerator.fromRequest(customerId, items, consignee, message);
    }
}
