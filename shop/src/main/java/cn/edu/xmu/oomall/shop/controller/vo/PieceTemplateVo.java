package cn.edu.xmu.oomall.shop.controller.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class PieceTemplateVo extends RegionTemplateVo{
    private Integer firstItem;

    private Long firstItemPrice;

    private Integer additionalItems;

    private Long additionalItemsPrice;

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
    public Integer getUnit() {
        return unit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }


    public Integer getFirstItems() {
        return firstItem;
    }
    public Long getFirstPrice() {
        return firstItemPrice;
    }
    public Integer getAdditionalItems() {
        return additionalItems;
    }
    public Long getAdditionalPrice() {
        return additionalItemsPrice;
    }


}
