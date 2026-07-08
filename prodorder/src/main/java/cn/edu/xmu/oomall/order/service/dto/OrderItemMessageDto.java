//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * MQ 消息中的订单明细，避免直接序列化 BO 导致反序列化类型丢失。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemMessageDto implements Serializable {

    private Long onsaleId;

    private Integer quantity;

    private Long price;

    private String name;

    private Long creatorId;

    private String creatorName;

    private Long actId;

    private Long couponId;
}
