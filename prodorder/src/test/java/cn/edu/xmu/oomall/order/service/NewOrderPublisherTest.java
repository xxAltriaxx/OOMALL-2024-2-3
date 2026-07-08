//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.service.dto.NewOrderMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class NewOrderPublisherTest {

    private static final String NEW_ORDER_TOPIC = "New-Order";

    @Mock
    private RocketMQTemplate rocketMQTemplate;

    @InjectMocks
    private NewOrderPublisher newOrderPublisher;

    @BeforeEach
    void setUp() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @AfterEach
    void tearDown() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void publishAfterCommit_nullOrderId_shouldNotSend() {
        newOrderPublisher.publishAfterCommit(null, List.of(orderItem(1L, 2)));

        verifyNoInteractions(rocketMQTemplate);
    }

    @Test
    void publishAfterCommit_nullItems_shouldNotSend() {
        newOrderPublisher.publishAfterCommit(100L, null);

        verifyNoInteractions(rocketMQTemplate);
    }

    @Test
    void publishAfterCommit_emptyItems_shouldNotSend() {
        newOrderPublisher.publishAfterCommit(100L, Collections.emptyList());

        verifyNoInteractions(rocketMQTemplate);
    }

    @Test
    void publishAfterCommit_noActiveTransaction_shouldSendImmediately() {
        List<OrderItem> items = List.of(orderItem(10L, 3));

        newOrderPublisher.publishAfterCommit(100L, items);

        assertSentMessage(100L, 10L, 3);
    }

    @Test
    void publishAfterCommit_activeTransaction_shouldSendOnlyAfterCommit() {
        TransactionSynchronizationManager.initSynchronization();
        try {
            newOrderPublisher.publishAfterCommit(200L, List.of(orderItem(20L, 1)));

            verify(rocketMQTemplate, never()).syncSend(eq(NEW_ORDER_TOPIC), org.mockito.ArgumentMatchers.anyString());

            List<TransactionSynchronization> synchronizations =
                    TransactionSynchronizationManager.getSynchronizations();
            assertEquals(1, synchronizations.size());
            synchronizations.get(0).afterCommit();

            assertSentMessage(200L, 20L, 1);
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void publishAfterCommit_sameOnsale_shouldMergeQuantity() {
        List<OrderItem> items = Arrays.asList(
                orderItem(30L, 2),
                orderItem(30L, 3),
                orderItem(40L, 1)
        );

        newOrderPublisher.publishAfterCommit(300L, items);

        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(rocketMQTemplate).syncSend(eq(NEW_ORDER_TOPIC), payloadCaptor.capture());

        NewOrderMessage message = JacksonUtil.toObj(payloadCaptor.getValue(), NewOrderMessage.class);
        assertEquals(300L, message.getId());
        assertEquals(2, message.getOrderItems().size());
        assertEquals(5, findQuantity(message, 30L));
        assertEquals(1, findQuantity(message, 40L));
    }

    @Test
    void publishAfterCommit_nullItem_shouldThrowBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> newOrderPublisher.publishAfterCommit(1L, Collections.singletonList(null)));

        assertEquals(ReturnNo.FIELD_NOTVALID, ex.getErrno());
        verifyNoInteractions(rocketMQTemplate);
    }

    @Test
    void publishAfterCommit_missingOnsaleId_shouldThrowBusinessException() {
        OrderItem item = orderItem(null, 1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> newOrderPublisher.publishAfterCommit(1L, List.of(item)));

        assertEquals(ReturnNo.FIELD_NOTVALID, ex.getErrno());
        verifyNoInteractions(rocketMQTemplate);
    }

    @Test
    void publishAfterCommit_missingQuantity_shouldThrowBusinessException() {
        OrderItem item = orderItem(1L, null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> newOrderPublisher.publishAfterCommit(1L, List.of(item)));

        assertEquals(ReturnNo.FIELD_NOTVALID, ex.getErrno());
        verifyNoInteractions(rocketMQTemplate);
    }

    @Test
    void publishAfterCommit_messagingException_shouldNotPropagate() {
        doThrow(new MessagingException("mq down"))
                .when(rocketMQTemplate)
                .syncSend(eq(NEW_ORDER_TOPIC), org.mockito.ArgumentMatchers.anyString());

        newOrderPublisher.publishAfterCommit(400L, List.of(orderItem(50L, 2)));

        verify(rocketMQTemplate).syncSend(eq(NEW_ORDER_TOPIC), org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void publishAfterCommit_runtimeException_shouldNotPropagate() {
        doThrow(new IllegalStateException("send failed"))
                .when(rocketMQTemplate)
                .syncSend(eq(NEW_ORDER_TOPIC), org.mockito.ArgumentMatchers.anyString());

        newOrderPublisher.publishAfterCommit(500L, List.of(orderItem(60L, 1)));

        verify(rocketMQTemplate).syncSend(eq(NEW_ORDER_TOPIC), org.mockito.ArgumentMatchers.anyString());
    }

    private void assertSentMessage(Long orderId, Long onsaleId, int quantity) {
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(rocketMQTemplate).syncSend(eq(NEW_ORDER_TOPIC), payloadCaptor.capture());

        NewOrderMessage message = JacksonUtil.toObj(payloadCaptor.getValue(), NewOrderMessage.class);
        assertEquals(orderId, message.getId());
        assertEquals(1, message.getOrderItems().size());
        assertEquals(quantity, findQuantity(message, onsaleId));
    }

    private int findQuantity(NewOrderMessage message, Long onsaleId) {
        return message.getOrderItems().stream()
                .filter(item -> onsaleId.equals(item.getOnsaleId()))
                .findFirst()
                .orElseThrow()
                .getQuantity();
    }

    private OrderItem orderItem(Long onsaleId, Integer quantity) {
        return OrderItem.builder()
                .onsaleId(onsaleId)
                .quantity(quantity)
                .build();
    }
}
