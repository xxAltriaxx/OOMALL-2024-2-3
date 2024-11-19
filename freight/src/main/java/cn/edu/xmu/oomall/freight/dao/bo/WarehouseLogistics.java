package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseLogisticsVo;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.mapper.po.WarehouseLogisticsPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({WarehouseLogisticsVo.class, WarehouseLogisticsPo.class})
public class WarehouseLogistics extends OOMallObject implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(WarehouseLogistics.class);
    public static Byte VALID = 0;

    public static Byte INVALID = 1;

    // 检查warehouse存在且Valid
    public void buildWarehouse(Long shopId) throws RuntimeException {
        if(this.warehouseId == null){
            throw new NullPointerException("warehouseId is null");
        }
        if(this.warehouseDao == null){
            throw new NullPointerException("warehouseDao is null");
        }
        // 若不存在warehouse或其与shopId不对应，则抛出异常
        Warehouse warehouse = this.warehouseDao.findById(shopId, this.warehouseId);
    }

    // build & 检查shopLogistics存在且Valid
    public void buildShopLogistics(Long shopId) throws RuntimeException {
        if(this.shopLogisticsId == null){
            throw new NullPointerException("shopLogisticsId is null");
        }
        if(this.shopLogisticsDao == null){
            throw new NullPointerException("shopLogisticsDao is null");
        }
        this.shopLogistics = this.shopLogisticsDao.findById(shopId, this.shopLogisticsId);
        this.shopLogisticsDao.build(this.shopLogistics);
        this.shopLogistics.buildLogistics();
    }

    @JsonIgnore
    @ToString.Exclude
    public ShopLogistics shopLogistics;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private WarehouseDao warehouseDao;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private ShopLogisticsDao shopLogisticsDao;

    private Long warehouseId;

    private Long shopLogisticsId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Byte invalid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
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

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getShopLogisticsId() {
        return shopLogisticsId;
    }

    public void setShopLogisticsId(Long shopLogisticsId) {
        this.shopLogisticsId = shopLogisticsId;
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

    public ShopLogistics getShopLogistics() {
        return shopLogistics;
    }

    public void setShopLogistics(ShopLogistics shopLogistics) {
        this.shopLogistics = shopLogistics;
    }
}
