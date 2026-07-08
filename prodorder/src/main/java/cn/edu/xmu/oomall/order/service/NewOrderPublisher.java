//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
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

import java.util.ArrayList;
import java.util.List;

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
        if (orderId == null || orderItems == null || orderItems.isEmpty()) {
            return;
        }
        Runnable publishTask = () -> doPublish(orderId, orderItems);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new AfterCommitSynchronization(publishTask));
            return;
        }
        publishTask.run();
    }

    private void doPublish(Long orderId, List<OrderItem> orderItems) {
        List<NewOrderItemMessage> messageItems = new ArrayList<>();
        for (OrderItem item : orderItems) {
            if (item == null || item.getOnsaleId() == null || item.getQuantity() == null) {
                throw new BusinessException(ReturnNo.FIELD_NOTVALID, "订单明细缺少 onsaleId 或 quantity");
            }
            messageItems.add(NewOrderItemMessage.builder()
                    .id(item.getOnsaleId())
                    .quantity(item.getQuantity())
                    .build());
        }
        NewOrderMessage message = NewOrderMessage.builder()
                .id(orderId)
                .orderItems(messageItems)
                .build();
        String payload = JacksonUtil.toJson(message);
        try {
            rocketMQTemplate.syncSend(NEW_ORDER_TOPIC, payload);
            logger.info("已发送库存扣减消息，orderId={}，itemCount={}", orderId, orderItems.size());
        } catch (Exception ex) {
            logger.error("发送库存扣减消息失败，orderId={}，itemCount={}", orderId, messageItems.size(), ex);
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,
                    String.format("发送库存扣减消息失败，orderId=%d", orderId));
        }
    }

    private static final class AfterCommitSynchronization implements TransactionSynchronization {
        private final Runnable task;

        private AfterCommitSynchronization(Runnable task) {
            this.task = task;
        }

        @Override
        public void afterCommit() {
            task.run();
        }
    }
}
