package cn.edu.xmu.oomall.ztoexpress.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelPreOrderVo {
    private String cancelType;
    private String orderCode;
    private String billCode;
}
