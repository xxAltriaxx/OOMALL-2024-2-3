package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({WarehouseLogistics.class})
public class WarehouseLogisticsDto {
    public WarehouseLogisticsDto(WarehouseLogistics warehouseLogistics){
        this();
        this.shopLogistics = CloneFactory.copy(new ShopLogisticsDto(warehouseLogistics.getShopLogistics()), warehouseLogistics.getShopLogistics());
        this.setCreator(IdNameTypeDto.builder().id(warehouseLogistics.getCreatorId())
                .name(warehouseLogistics.getCreatorName()).build());
        this.setModifier(IdNameTypeDto.builder().id(warehouseLogistics.getModifierId())
                .name(warehouseLogistics.getModifierName()).build());
    }
    private ShopLogisticsDto shopLogistics;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Byte invalid;

    private IdNameTypeDto creator;

    private IdNameTypeDto modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public ShopLogisticsDto getShopLogistics() {
        return shopLogistics;
    }

    public void setShopLogistics1(ShopLogisticsDto shopLogisticsdto) {
        this.shopLogistics = shopLogisticsdto;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Byte getInvalid() {
        return invalid;
    }

    public void setInvalid(Byte invalid) {
        this.invalid = invalid;
    }

    public IdNameTypeDto getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeDto creator) {
        this.creator = creator;
    }

    public IdNameTypeDto getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeDto modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
