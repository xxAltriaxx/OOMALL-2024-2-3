package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 2023-dgn3-009
 * @author huangzian
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({Express.class})
public class ExpressDto {
    private Long id;
    private String billCode;
    private IdNameTypeDto logistics;
    private ContactsDto shipper;
    private ContactsDto receiver;
    private Byte status;
    private IdNameTypeDto creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeDto modifier;

    public ExpressDto(Express express){
        super();
        CloneFactory.copy(this, express);
        this.creator = IdNameTypeDto.builder().id(express.getCreatorId()).name(express.getCreatorName()).build();
        this.modifier = IdNameTypeDto.builder().id(express.getModifierId()).name(express.getModifierName()).build();
        this.shipper = ContactsDto.builder().name(express.getSendName()).mobile(express.getSendMobile())
                .address(express.getSendAddress()).regionId(express.getSendRegionId()).build();
        this.receiver = ContactsDto.builder().name(express.getReceivName()).mobile(express.getReceivMobile())
                .address(express.getReceivAddress()).regionId(express.getReceivRegionId()).build();
        this.logistics =IdNameTypeDto.builder().id(express.getShopLogisticsId())
                .name(express.getShopLogistics().getLogistics().getName()).build();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public IdNameTypeDto getLogistics() {
        return logistics;
    }

    public void setLogistics(IdNameTypeDto logistics) {
        this.logistics = logistics;
    }

    public ContactsDto getShipper() {
        return shipper;
    }

    public void setShipper(ContactsDto shipper) {
        this.shipper = shipper;
    }

    public ContactsDto getReceiver() {
        return receiver;
    }

    public void setReceiver(ContactsDto receiver) {
        this.receiver = receiver;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public IdNameTypeDto getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeDto creator) {
        this.creator = creator;
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

    public IdNameTypeDto getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeDto modifier) {
        this.modifier = modifier;
    }

}
