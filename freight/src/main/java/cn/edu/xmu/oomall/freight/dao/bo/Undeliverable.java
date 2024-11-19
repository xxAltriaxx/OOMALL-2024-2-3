package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.controller.vo.UndeliverableVo;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import cn.edu.xmu.oomall.freight.mapper.po.UndeliverablePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({UndeliverablePo.class, UndeliverableVo.class})
public class Undeliverable extends OOMallObject implements Serializable {
    public void buildRegion() throws RuntimeException{
        if(this.regionId == null){
            throw new NullPointerException("RegionId is null");
        }
        if(this.regionDao == null){
            throw new NullPointerException("RegionDao is null");
        }
        this.region = this.regionDao.findRegionById(this.regionId);
    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionDao regionDao;

    @ToString.Exclude
    @JsonIgnore
    private Region region;

    private Long regionId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long shopLogisticsId;

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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
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

    public Long getShopLogisticsId() {
        return shopLogisticsId;
    }

    public void setShopLogisticsId(Long shopLogisticsId) {
        this.shopLogisticsId = shopLogisticsId;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region1) {
        this.region = region1;
    }
}
