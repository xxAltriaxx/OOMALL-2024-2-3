//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderItemMessage implements Serializable {

    /**
     * 对应 onsaleId，goods-service 通过 Item.id 扣减库存。
     */
    private Long id;

    private Integer quantity;
}
