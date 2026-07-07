//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.util;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于用户与下单请求内容生成稳定幂等键，防止重复提交产生多笔订单。
 */
public final class OrderIdempotentKeyGenerator {

    private OrderIdempotentKeyGenerator() {
    }

    public static String fromRequest(Long customerId, List<OrderItemDto> items, ConsigneeDto consignee, String message) {
        List<OrderItemDto> sortedItems = items.stream()
                .sorted(Comparator
                        .comparing(OrderItemDto::getOnsaleId, Comparator.nullsLast(Long::compareTo))
                        .thenComparing(OrderItemDto::getQuantity, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(OrderItemDto::getActId, Comparator.nullsLast(Long::compareTo))
                        .thenComparing(OrderItemDto::getCouponId, Comparator.nullsLast(Long::compareTo)))
                .collect(Collectors.toList());

        String payload = customerId + "|"
                + JacksonUtil.toJson(sortedItems) + "|"
                + consignee.getConsignee() + "|"
                + consignee.getAddress() + "|"
                + consignee.getMobile() + "|"
                + consignee.getRegionId() + "|"
                + Objects.toString(message, "");
        return sha256Hex(payload);
    }

    private static String sha256Hex(String payload) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 不可用", ex);
        }
    }
}
