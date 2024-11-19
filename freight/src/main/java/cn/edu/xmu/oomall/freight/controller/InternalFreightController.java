package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.SendPackageVo;
import cn.edu.xmu.oomall.freight.service.ExpressService;
import cn.edu.xmu.oomall.freight.controller.dto.ExpressDto;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.controller.dto.SimpleExpressDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
public class InternalFreightController {
    private final Logger logger = LoggerFactory.getLogger(InternalFreightController.class);
    private ExpressService expressService;

    @Autowired
    public InternalFreightController(ExpressService expressService) {
        this.expressService = expressService;
    }

    /**
     * 创建运单
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    @PostMapping("/shops/{shopId}/packages")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject createPackage(@PathVariable Long shopId,
                                      @Validated @RequestBody ExpressVo expressVo,
                                      @LoginUser UserDto user) {
        Express express = Express.builder().sendRegionId(expressVo.getSender().getRegionId())
                .sendAddress(expressVo.getSender().getAddress())
                .sendMobile(expressVo.getSender().getMobile())
                .sendName(expressVo.getSender().getName())
                .receivRegionId(expressVo.getDelivery().getRegionId())
                .receivAddress(expressVo.getDelivery().getAddress())
                .receivMobile(expressVo.getDelivery().getMobile())
                .receivName(expressVo.getDelivery().getName())
                .shopLogisticsId(expressVo.getShopLogisticId())
                .goodsType(expressVo.getGoodsType()).weight(expressVo.getWeight()).build();
        Express newExpress = this.expressService.createExpress(shopId, express, user);
        return new ReturnObject(ReturnNo.CREATED, CloneFactory.copy(new SimpleExpressDto(), newExpress));
    }

    /**
     * 根据运单号获取运单信息
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    @GetMapping("/shops/{shopId}/packages")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getPackage(@PathVariable Long shopId, @RequestParam String billCode) {
        Express express = this.expressService.findExpressByBillCode(shopId, billCode);
        if (express != null) return new ReturnObject(new ExpressDto(express));
        else return new ReturnObject();
    }

    /**
     * 根据运单id获取运单信息
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    @GetMapping("/packages/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getPackageById(@PathVariable Long id) {
        Express express = this.expressService.findExpressById(PLATFORM, id);
        return new ReturnObject(new ExpressDto(express));
    }

    /**
     * 商户验收快递
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    @PutMapping("/shops/{shopId}/packages/{id}/confirm")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject confirmPackage(@PathVariable Long shopId,
                                       @PathVariable Long id,
                                       @RequestBody Map<String, Object> body,
                                       @LoginUser UserDto user) {
        Byte status = Byte.parseByte((String) body.get("status"));
        this.expressService.confirmExpress(shopId, id, status, user);
        return new ReturnObject();
    }

    /**
     * 商户取消运单
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    @PutMapping("/shops/{shopId}/packages/{id}/cancel")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject cancelPackage(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user) {
        this.expressService.cancelExpress(shopId, id, user);
        return new ReturnObject();
    }

    /**
     * 商户发出揽件
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    @PutMapping("/shops/{shopId}/packages/{id}/send")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject sendPackage(@PathVariable Long shopId, @PathVariable Long id,
                                    @LoginUser UserDto user, @RequestBody SendPackageVo vo) {
        this.expressService.sendExpress(shopId, id, user, vo.getStartTime(), vo.getEndTime());
        return new ReturnObject();
    }
}
