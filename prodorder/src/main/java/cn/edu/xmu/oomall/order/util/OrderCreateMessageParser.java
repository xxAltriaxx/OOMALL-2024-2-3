//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.util;

import cn.edu.xmu.oomall.order.service.dto.OrderCreateMessage;
import cn.edu.xmu.oomall.order.service.dto.OrderItemMessageDto;
import cn.edu.xmu.oomall.order.service.dto.OrderPackMessageDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class OrderCreateMessageParser {

    private static final Logger logger = LoggerFactory.getLogger(OrderCreateMessageParser.class);

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final TypeReference<OrderCreateMessage> MESSAGE_TYPE = new TypeReference<>() {
    };

    private OrderCreateMessageParser() {
    }

    public static OrderCreateMessage parse(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            OrderCreateMessage message = MAPPER.readValue(body, MESSAGE_TYPE);
            return isValid(message) ? message : null;
        } catch (IOException ex) {
            logger.error("订单事务消息反序列化失败: {}", body, ex);
            return null;
        }
    }

    private static boolean isValid(OrderCreateMessage message) {
        if (message == null || message.getIdempotentKey() == null || message.getIdempotentKey().isBlank()) {
            return false;
        }
        if (message.getPacks() == null || message.getPacks().isEmpty()) {
            return false;
        }
        if (message.getConsignee() == null || message.getUser() == null) {
            return false;
        }
        for (OrderPackMessageDto pack : message.getPacks()) {
            if (pack.getShopId() == null || pack.getItems() == null || pack.getItems().isEmpty()) {
                return false;
            }
            for (OrderItemMessageDto item : pack.getItems()) {
                if (item.getOnsaleId() == null || item.getQuantity() == null || item.getPrice() == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
