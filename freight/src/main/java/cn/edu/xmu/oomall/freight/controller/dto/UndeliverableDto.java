package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.oomall.freight.dao.UndeliverableDao;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({Undeliverable.class})
public class UndeliverableDto {
    public UndeliverableDto(Undeliverable undeliverable){
        this();
        this.setRegion1(IdNameTypeDto.builder().id(undeliverable.getRegionId())
                .name(undeliverable.getRegion().getName()).build());
        this.setCreator(IdNameTypeDto.builder().id(undeliverable.getCreatorId())
                .name(undeliverable.getCreatorName()).build());
        this.setModifier(IdNameTypeDto.builder().id(undeliverable.getModifierId())
                .name(undeliverable.getModifierName()).build());
    }

    private Long id;

    private IdNameTypeDto region;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private IdNameTypeDto creator;

    private IdNameTypeDto modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdNameTypeDto getRegion() {
        return region;
    }

    public void setRegion1(IdNameTypeDto region) {
        this.region = region;
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
