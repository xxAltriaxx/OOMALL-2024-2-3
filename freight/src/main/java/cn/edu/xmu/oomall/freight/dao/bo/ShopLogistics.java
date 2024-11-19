package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.ModifyShopLogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.dao.LogisticsDao;

import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({ShopLogisticsPo.class, ShopLogisticsVo.class, ModifyShopLogisticsVo.class})
public class ShopLogistics extends OOMallObject implements Serializable {
    public static Byte VALID = 0;
    public static Byte INVALID = 1;

    public void setValidity(Byte status){
        this.invalid = status;
    }

    public void buildLogistics() throws RuntimeException{
        if(this.logisticsId == null){
            throw new NullPointerException("LogisticsId is null");
        }
        if(this.logisticsDao == null){
            throw new NullPointerException("LogisticsDao is null");
        }
        // 若不存在Logistics，则抛出异常
        this.logistics = this.logisticsDao.findById(this.logisticsId);
    }

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private LogisticsDao logisticsDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ExpressDao expressDao;

    @ToString.Exclude
    @JsonIgnore
    private LogisticsAdaptor logisticsAdaptor;

    public void setLogisticsAdaptor(LogisticsAdaptorFactory factory) throws RuntimeException{
        this.buildLogistics();
        this.logisticsAdaptor = factory.createLogisticAdaptor(this.getLogistics());
    }

    /**
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    private static final Logger logger = LoggerFactory.getLogger(ShopLogistics.class);
    public Express createExpress(Long shopId, Express express, UserDto user){
        express.setShopId(shopId);
        express.setStatus(Express.UNSHIPPED);
        logger.debug("createExpress: logisticsAdaptor = {}", logisticsAdaptor);
        PostCreatePackageAdaptorDto adaptorDto = this.logisticsAdaptor.createPackage(this, express);
        if (adaptorDto.getBillCode() != null) {
            express.setBillCode(adaptorDto.getBillCode());
        }
        this.expressDao.insert(express, user);
        logger.debug("createExpress: dto = {}", adaptorDto);
        return express;
    }

    //假设account为属性
    private String account;

    @JsonIgnore
    @ToString.Exclude
    private Logistics logistics;

    private Long shopId;

    private Long logisticsId;

    private String secret;

    private Byte invalid;

    private Integer priority;

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Long logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Byte getInvalid() {
        return invalid;
    }

    public void setInvalid(Byte invalid) {
        this.invalid = invalid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    public String getAccount() {
        return account;
    }

}
