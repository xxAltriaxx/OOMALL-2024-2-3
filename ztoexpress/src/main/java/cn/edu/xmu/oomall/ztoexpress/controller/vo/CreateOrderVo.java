package cn.edu.xmu.oomall.ztoexpress.controller.vo;

import cn.edu.xmu.oomall.ztoexpress.unit.PersonInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderVo {
    private String partnerType;
    private String orderType;
    private String partnerOrderCode;
    private PersonInfo senderInfo;
    private PersonInfo receiverInfo;
}
