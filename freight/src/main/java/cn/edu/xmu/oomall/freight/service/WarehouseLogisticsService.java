package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class WarehouseLogisticsService {
    private final Logger logger = LoggerFactory.getLogger(WarehouseLogisticsService.class);

    private WarehouseLogisticsDao warehouseLogisticsDao;

    @Autowired
    public WarehouseLogisticsService(WarehouseLogisticsDao warehouseLogisticsDao) {
        this.warehouseLogisticsDao = warehouseLogisticsDao;
    }

    /**
     * 新建仓库物流: 仓库物流的仓库id和商铺物流的id必须存在
     * @param warehouseLogistics
     * @param shopId
     * @param shopLogisticsId
     * @param user
     * @throws Exception
     */
    public void addWarehouseLogistics(WarehouseLogistics warehouseLogistics, Long shopId, Long shopLogisticsId, UserDto user){
        warehouseLogisticsDao.insert(warehouseLogistics, shopId, shopLogisticsId, user);
    }

    public void alterWarehouseLogistics(WarehouseLogistics warehouseLogistics, Long shopId, Long warehouseLogisticsId, UserDto user){
        WarehouseLogistics savedBo = this.warehouseLogisticsDao.findById(warehouseLogisticsId);
        warehouseLogistics.setShopLogisticsId(savedBo.getShopLogisticsId());
        warehouseLogisticsDao.save(warehouseLogistics, shopId, warehouseLogisticsId, user);
    }

    public void deleteWarehouseLogistics(Long shopId, Long warehouseId, Long warehouseLogisticsId, UserDto user){
        WarehouseLogistics savedBo = this.warehouseLogisticsDao.findById(warehouseLogisticsId);
        warehouseLogisticsDao.delete(savedBo, shopId, warehouseId);
    }

    public List<WarehouseLogistics> retrieveWarehouseLogistics(Long shopId, Long warehouseId, Integer page, Integer pageSize, UserDto user){
        return warehouseLogisticsDao.retrieveByShopIdAndWarehouseId(shopId, warehouseId, page, pageSize);
    }
}
