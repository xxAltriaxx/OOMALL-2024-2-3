//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderVo {

    @NotEmpty(message = "订单明细不能为空")
    @Valid
    private List<OrderItemVo> items;

    @NotBlank(message = "联系人不能为空")
    private String consignee;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotNull(message = "地区不能为空")
    private Long regionId;

    @NotBlank(message = "联系电话不能为空")
    private String mobile;

    private String message;

    /**
     * 客户端可选幂等键；未传时服务端按用户与请求内容生成。
     */
    @Size(max = 64, message = "幂等键长度不能超过64")
    private String idempotentKey;

}
