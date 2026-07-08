//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 发送给 goods-service 的新订单消息，字段结构与 product 模块 NewOrderConsumer 保持一致。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderMessage implements Serializable {

    private Long id;

    private List<NewOrderItemMessage> orderItems;
}
