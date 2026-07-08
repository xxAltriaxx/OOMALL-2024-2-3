//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * MQ 消息中的店铺订单包，使用显式 shopId 字段避免 Map 键反序列化为 String。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPackMessageDto implements Serializable {

    private Long shopId;

    private List<OrderItemMessageDto> items;
}
