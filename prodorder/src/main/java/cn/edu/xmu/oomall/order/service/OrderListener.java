//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.order.service.dto.OrderCreateMessage;
import cn.edu.xmu.oomall.order.util.OrderCreateMessageParser;
import cn.edu.xmu.oomall.order.util.OrderMessageConverter;
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
        return completeOrderTransaction(msg, "本地事务");
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return completeOrderTransaction(msg, "事务回查");
    }

    private RocketMQLocalTransactionState completeOrderTransaction(Message msg, String phase) {
        OrderCreateMessage orderCreateMessage = parseMessage(msg);
        if (orderCreateMessage == null || orderCreateMessage.getPacks() == null) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        orderService.prepareForTransaction(
                orderCreateMessage.getIdempotentKey(),
                orderCreateMessage.getPacks().size());

        RocketMQLocalTransactionState existingState = orderService.resolveTransactionState(orderCreateMessage);
        if (existingState == RocketMQLocalTransactionState.COMMIT) {
            logger.info("{}：订单已完成，idempotentKey={}", phase, orderCreateMessage.getIdempotentKey());
            return RocketMQLocalTransactionState.COMMIT;
        }
        if (existingState == RocketMQLocalTransactionState.ROLLBACK) {
            logger.info("{}：订单已失败，idempotentKey={}", phase, orderCreateMessage.getIdempotentKey());
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        try {
            orderService.saveOrder(
                    orderCreateMessage.getIdempotentKey(),
                    OrderMessageConverter.toPacks(orderCreateMessage.getPacks()),
                    orderCreateMessage.getConsignee(),
                    orderCreateMessage.getMessage(),
                    orderCreateMessage.getUser());
            logger.info("{}：订单落库成功，idempotentKey={}", phase, orderCreateMessage.getIdempotentKey());
            return RocketMQLocalTransactionState.COMMIT;
        } catch (BusinessException e) {
            logger.error("{}：订单业务校验失败，idempotentKey={}", phase, orderCreateMessage.getIdempotentKey(), e);
            orderService.failOrderTransaction(orderCreateMessage.getIdempotentKey());
            return RocketMQLocalTransactionState.ROLLBACK;
        } catch (Exception e) {
            logger.error("{}：订单落库未完成，等待重试，idempotentKey={}", phase, orderCreateMessage.getIdempotentKey(), e);
            return RocketMQLocalTransactionState.UNKNOWN;
        }
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

        OrderCreateMessage orderCreateMessage = OrderCreateMessageParser.parse(body);
        if (orderCreateMessage == null) {
            logger.error("订单事务消息反序列化失败: {}", body);
        }
        return orderCreateMessage;
    }
}
