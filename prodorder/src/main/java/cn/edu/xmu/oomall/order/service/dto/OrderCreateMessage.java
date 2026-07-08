//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateMessage implements Serializable {

    /**
     * 幂等键，同一创建请求重试时保持不变
     */
    private String idempotentKey;

    private List<OrderPackMessageDto> packs;

    private ConsigneeDto consignee;

    private String message;

    private UserDto user;
}
