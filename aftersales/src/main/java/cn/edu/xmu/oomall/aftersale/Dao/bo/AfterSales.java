package cn.edu.xmu.oomall.aftersale.Dao.bo;

import cn.edu.xmu.oomall.aftersale.Dao.Solutions.AfterSalesSolution;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    String state;
    String type;

    @ToString.Exclude
    @JsonIgnore
    AfterSalesSoluteProgress afterSalesSoluteProgress;

    @ToString.Exclude
    @JsonIgnore
    AfterSalesSolution afterSalesSolution;

    public AfterSalesSoluteProgress getAfterSalesSoluteProgress() {
        //若已经将Progress联系到该bo则直接返回
        if(afterSalesSoluteProgress != null){
            return afterSalesSoluteProgress;
        }
        //否则先使用工厂生产bo的type对应的solution
        this.afterSalesSolution=
    }
}
