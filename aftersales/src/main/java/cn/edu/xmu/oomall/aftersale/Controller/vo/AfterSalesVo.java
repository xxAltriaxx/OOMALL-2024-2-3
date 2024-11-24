package cn.edu.xmu.oomall.aftersale.Controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSalesVo {
    String reason;
    String conclusion;
    Integer quantity;
    String address;
    String contact;
    String mobile;
    String serialNo;
    String type;
    Long shopId;
}
