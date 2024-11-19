package cn.edu.xmu.oomall.freight.controller;
import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.dto.WarehouseLogisticsDto;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseLogisticsVo;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.service.WarehouseLogisticsService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@RestController
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class WarehouseLogisticsController {
    private final Logger logger = LoggerFactory.getLogger(WarehouseLogisticsController.class);

    private WarehouseLogisticsService warehouseLogisticsService;

    @Autowired
    public WarehouseLogisticsController(WarehouseLogisticsService warehouseLogisticsService) {
        this.warehouseLogisticsService = warehouseLogisticsService;
    }

    /**
     * 商户新建仓库物流
     * @param shopId
     * @param id 仓库id
     * @param lid 商铺物流id
     * @param vo
     * @return
     */
    @PostMapping("/warehouses/{id}/shoplogistics/{lid}")
    @Audit(departName = "shops")
    public ReturnObject addWarehouseLogistics(@PathVariable Long shopId,
                                         @PathVariable Long id,
                                         @PathVariable Long lid,
                                         @RequestBody WarehouseLogisticsVo vo,
                                         @LoginUser UserDto user){
        logger.debug("vo = {}",vo);
        WarehouseLogistics warehouseLogistics = CloneFactory.copy(new WarehouseLogistics(), vo);
        warehouseLogistics.setWarehouseId(id);
        logger.debug("warehouseLogistics = {}",warehouseLogistics);
        warehouseLogisticsService.addWarehouseLogistics(warehouseLogistics, shopId, lid, user);
        return new ReturnObject(ReturnNo.CREATED);
    }

    /**
     * 商铺修改仓库物流信息
     * @param shopId
     * @param id 仓库id
     * @param lid 商铺物流id
     * @param vo
     */
    @PutMapping("/warehouses/{id}/shoplogistics/{lid}")
    @Audit(departName = "shops")
    public ReturnObject alterWarehouseLogistics(@PathVariable Long shopId,
                                           @PathVariable Long id,
                                           @PathVariable Long lid,
                                           @RequestBody WarehouseLogisticsVo vo,
                                           @LoginUser UserDto user){
        WarehouseLogistics warehouseLogistics = CloneFactory.copy(new WarehouseLogistics(), vo);
        warehouseLogistics.setWarehouseId(id);
        logger.debug("warehouseLogistics = {}",warehouseLogistics);
        warehouseLogisticsService.alterWarehouseLogistics(warehouseLogistics, shopId, lid, user);
        return new ReturnObject();
    }

    /**
     * 商铺删除仓库物流
     *
     */
    @DeleteMapping("/warehouses/{id}/shoplogistics/{lid}")
    @Audit(departName = "shops")
    public ReturnObject deleteWarehouseLogistics(@PathVariable Long shopId,
                                            @PathVariable Long id,
                                            @PathVariable Long lid,
                                            @LoginUser UserDto user){

        warehouseLogisticsService.deleteWarehouseLogistics(shopId, id, lid, user);
        return new ReturnObject();
    }

    /**
     * 查询仓库物流
     * @param shopId
     * @param id
     * @param page  (not required)
     * @param pageSize (not required)
     * @return
     */
    @GetMapping("/warehouses/{id}/shoplogistics")
    @Audit(departName = "shops")
    public ReturnObject getWarehouseLogistics(@PathVariable Long shopId,
                                              @PathVariable Long id,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                              @LoginUser UserDto user){
        List<WarehouseLogistics> warehouseLogistics = warehouseLogisticsService.retrieveWarehouseLogistics(shopId, id, page, pageSize, user);
        List<WarehouseLogisticsDto> warehouseLogisticsDtoList = warehouseLogistics.stream()
                .map(warehouseLogistic -> CloneFactory.copy(new WarehouseLogisticsDto(warehouseLogistic), warehouseLogistic)).collect(java.util.stream.Collectors.toList());
        return new ReturnObject(new PageDto<>(warehouseLogisticsDtoList, page, pageSize));
    }

}
