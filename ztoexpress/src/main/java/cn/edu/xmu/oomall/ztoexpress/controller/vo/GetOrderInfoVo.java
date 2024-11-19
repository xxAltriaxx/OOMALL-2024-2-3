package cn.edu.xmu.oomall.ztoexpress.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderInfoVo {
    private String type;
    private String orderCode;
    private String billCode;
}
