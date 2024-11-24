package cn.edu.xmu.oomall.aftersale.Service.bo;

import cn.edu.xmu.oomall.aftersale.Service.Solutions.AfterSalesSolution;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AfterSales {
    Long id;
    String reason;
    String conclusion;
    int quantity;
    String address;
    String contact;
    String mobile;
    int in_arbitrated;
    String serialNo;
    int state;//0初始态，1等待商家处理申请中，2商家接受请求,3售后处理中，3售后完成，4售后被商家否决，5终止态
    String type;
    Long shop_id;

    Long afterSalesHistoryId;

    @ToString.Exclude
    @JsonIgnore
    AfterSalesHistory afterSalesHistory;

    @ToString.Exclude
    @JsonIgnore
    AfterSalesSolution afterSalesSolution;

}
