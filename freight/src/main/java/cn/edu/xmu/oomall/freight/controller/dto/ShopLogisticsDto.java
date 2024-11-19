package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.lettuce.core.StrAlgoArgs;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({ShopLogistics.class})
public class ShopLogisticsDto {
    public ShopLogisticsDto(ShopLogistics shopLogistics){
        this();
        this.setLogistics1(IdNameTypeDto.builder().id(shopLogistics.getLogistics().getId())
                .name(shopLogistics.getLogistics().getName()).build());
        this.setCreator(IdNameTypeDto.builder().id(shopLogistics.getCreatorId())
                .name(shopLogistics.getCreatorName()).build());
        this.setModifier(IdNameTypeDto.builder().id(shopLogistics.getModifierId())
                .name(shopLogistics.getModifierName()).build());
    }

    private Long id;

    private IdNameTypeDto logistics;

    private Byte invalid;

    private String secret;

    private Integer priority;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameTypeDto creator;

    private IdNameTypeDto modifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdNameTypeDto getLogistics() {
        return logistics;
    }

    public void setLogistics1(IdNameTypeDto logistics) {
        this.logistics = logistics;
    }

    public Byte getInvalid() {
        return invalid;
    }

    public void setInvalid(Byte invalid) {
        this.invalid = invalid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
}
