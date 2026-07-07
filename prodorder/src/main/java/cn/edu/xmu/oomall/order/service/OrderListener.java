//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.service.dto.OrderCreateMessage;
import cn.edu.xmu.oomall.order.service.exception.OrderCreationInProgressException;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQTransactionListener
public class OrderListener implements RocketMQLocalTransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderListener.class);

    private final OrderService orderService;

    @Autowired
    public OrderListener(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 事务消息发送成功回调
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        OrderCreateMessage orderCreateMessage = parseMessage(msg);
        if (orderCreateMessage == null || orderCreateMessage.getPacks() == null) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        RocketMQLocalTransactionState existingState = orderService.resolveTransactionState(orderCreateMessage);
        if (existingState != RocketMQLocalTransactionState.ROLLBACK) {
            logger.info("订单事务消息已有处理状态，idempotentKey={}，state={}",
                    orderCreateMessage.getIdempotentKey(), existingState);
            return existingState;
        }

        try {
            orderService.saveOrder(
                    orderCreateMessage.getIdempotentKey(),
                    orderCreateMessage.getPacks(),
                    orderCreateMessage.getConsignee(),
                    orderCreateMessage.getMessage(),
                    orderCreateMessage.getUser());
        } catch (OrderCreationInProgressException e) {
            logger.info("订单创建进行中，等待回查，idempotentKey={}", orderCreateMessage.getIdempotentKey());
            return RocketMQLocalTransactionState.UNKNOWN;
        } catch (Exception e) {
            logger.error("保存订单失败，idempotentKey={}", orderCreateMessage.getIdempotentKey(), e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        OrderCreateMessage orderCreateMessage = parseMessage(msg);
        RocketMQLocalTransactionState state = orderService.resolveTransactionState(orderCreateMessage);
        logger.debug("订单事务回查，idempotentKey={}，state={}",
                orderCreateMessage == null ? null : orderCreateMessage.getIdempotentKey(), state);
        return state;
    }

    private OrderCreateMessage parseMessage(Message msg) {
        if (msg == null || msg.getPayload() == null) {
            logger.error("订单事务消息为空");
            return null;
        }

        String body;
        Object payload = msg.getPayload();
        if (payload instanceof byte[]) {
            body = new String((byte[]) payload, StandardCharsets.UTF_8);
        } else if (payload instanceof String) {
            body = (String) payload;
        } else {
            body = payload.toString();
        }

        OrderCreateMessage orderCreateMessage = JacksonUtil.toObj(body, OrderCreateMessage.class);
        if (orderCreateMessage == null || orderCreateMessage.getPacks() == null) {
            logger.error("订单事务消息反序列化失败: {}", body);
        }
        return orderCreateMessage;
    }
}
