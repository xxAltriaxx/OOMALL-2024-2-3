//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.service.dto.OrderCreateMessage;
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
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        OrderCreateMessage orderCreateMessage = JacksonUtil.toObj(body, OrderCreateMessage.class);
        if (orderCreateMessage == null || orderCreateMessage.getPacks() == null) {
            logger.error("订单事务消息反序列化失败: {}", body);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        try {
            orderService.saveOrder(
                    orderCreateMessage.getPacks(),
                    orderCreateMessage.getConsignee(),
                    orderCreateMessage.getMessage(),
                    orderCreateMessage.getUser());
        } catch (Exception e) {
            logger.error("保存订单失败", e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
