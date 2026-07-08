//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送给 goods-service 的新订单消息，字段结构与 product 模块 NewOrderConsumer 保持一致。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderMessage implements Serializable {

    /**
     * 店铺订单 ID，对应 goods-service 消费端 NewOrderMessage.id。
     */
    private Long id;

    /**
     * 待扣减库存的明细列表。
     */
    @Builder.Default
    private List<NewOrderItemMessage> orderItems = new ArrayList<>();
}
