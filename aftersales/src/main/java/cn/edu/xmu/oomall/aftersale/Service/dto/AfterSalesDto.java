package cn.edu.xmu.oomall.aftersale.Service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSalesDto {
    Long id;
    String reason;
    String conclusion;
    Integer quantity;
    String address;
    String contact;
    String mobile;
    String serialNo;
    String type;
    int in_arbitrated;
    int state;
    Long shopId;
    Long userId;
}
