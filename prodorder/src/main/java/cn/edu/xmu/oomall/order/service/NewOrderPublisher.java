//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.service.dto.NewOrderItemMessage;
import cn.edu.xmu.oomall.order.service.dto.NewOrderMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewOrderPublisher {

    private static final Logger logger = LoggerFactory.getLogger(NewOrderPublisher.class);

    private static final String NEW_ORDER_TOPIC = "New-Order";

    private final RocketMQTemplate rocketMQTemplate;

    @Autowired
    public NewOrderPublisher(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public void publishAfterCommit(Long orderId, List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return;
        }
        Runnable publishTask = () -> doPublish(orderId, orderItems);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishTask.run();
                }
            });
            return;
        }
        publishTask.run();
    }

    private void doPublish(Long orderId, List<OrderItem> orderItems) {
        NewOrderMessage message = NewOrderMessage.builder()
                .id(orderId)
                .orderItems(orderItems.stream()
                        .map(item -> NewOrderItemMessage.builder()
                                .id(item.getOnsaleId())
                                .quantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        String payload = JacksonUtil.toJson(message);
        rocketMQTemplate.syncSend(NEW_ORDER_TOPIC, payload);
        logger.info("已发送库存扣减消息，orderId={}，itemCount={}", orderId, orderItems.size());
    }
}
