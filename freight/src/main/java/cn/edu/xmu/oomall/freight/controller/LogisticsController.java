package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@RestController
public class LogisticsController {

    private LogisticsService logisticsService;

    @Autowired
    public LogisticsController(LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @GetMapping("/logistics")
    public ReturnObject getLogisticsCompany(@RequestParam(required = false) String billCode,
                                            @LoginUser UserDto user){
        Logistics logistics = this.logisticsService.findByBillCode(billCode);
        IdNameTypeDto company = IdNameTypeDto.builder().id(logistics.getId()).name(logistics.getName()).build();
        return new ReturnObject(company);
    }

}
