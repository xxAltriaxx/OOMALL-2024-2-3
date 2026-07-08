//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.util;

import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.service.dto.OrderItemMessageDto;
import cn.edu.xmu.oomall.order.service.dto.OrderPackMessageDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OrderMessageConverter {

    private OrderMessageConverter() {
    }

    public static List<OrderPackMessageDto> fromPacks(Map<Long, List<OrderItem>> packs) {
        List<OrderPackMessageDto> packMessages = new ArrayList<>(packs.size());
        for (Map.Entry<Long, List<OrderItem>> entry : packs.entrySet()) {
            List<OrderItemMessageDto> items = new ArrayList<>(entry.getValue().size());
            for (OrderItem orderItem : entry.getValue()) {
                items.add(fromOrderItem(orderItem));
            }
            packMessages.add(OrderPackMessageDto.builder()
                    .shopId(entry.getKey())
                    .items(items)
                    .build());
        }
        return packMessages;
    }

    public static Map<Long, List<OrderItem>> toPacks(List<OrderPackMessageDto> packMessages) {
        Map<Long, List<OrderItem>> packs = new HashMap<>();
        for (OrderPackMessageDto packMessage : packMessages) {
            List<OrderItem> items = new ArrayList<>(packMessage.getItems().size());
            for (OrderItemMessageDto itemMessage : packMessage.getItems()) {
                items.add(toOrderItem(itemMessage));
            }
            packs.put(packMessage.getShopId(), items);
        }
        return packs;
    }

    private static OrderItemMessageDto fromOrderItem(OrderItem orderItem) {
        return OrderItemMessageDto.builder()
                .onsaleId(orderItem.getOnsaleId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .name(orderItem.getName())
                .creatorId(orderItem.getCreatorId())
                .creatorName(orderItem.getCreatorName())
                .actId(orderItem.getActId())
                .couponId(orderItem.getCouponId())
                .build();
    }

    private static OrderItem toOrderItem(OrderItemMessageDto itemMessage) {
        return OrderItem.builder()
                .onsaleId(itemMessage.getOnsaleId())
                .quantity(itemMessage.getQuantity())
                .price(itemMessage.getPrice())
                .name(itemMessage.getName())
                .creatorId(itemMessage.getCreatorId())
                .creatorName(itemMessage.getCreatorName())
                .actId(itemMessage.getActId())
                .couponId(itemMessage.getCouponId())
                .build();
    }
}
