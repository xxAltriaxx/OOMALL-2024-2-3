//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
     * 对应 onsaleId，序列化字段名 id 与 goods-service NewOrderConsumer 保持一致。
     */
    @JsonProperty("id")
    private Long onsaleId;

    /**
     * 购买数量。
     */
    private Integer quantity;
}
