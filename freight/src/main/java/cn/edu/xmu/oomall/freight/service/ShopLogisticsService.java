package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.UndeliverableDao;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
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
public class ShopLogisticsService {
    private final Logger logger = LoggerFactory.getLogger(ShopLogisticsService.class);

    private ShopLogisticsDao shopLogisticsDao;

    private UndeliverableDao undeliverableDao;

    @Autowired
    public ShopLogisticsService(ShopLogisticsDao shopLogisticsDao, UndeliverableDao undeliverableDao){
        this.shopLogisticsDao = shopLogisticsDao;
        this.undeliverableDao = undeliverableDao;
    }

    /**
     * 新增商铺的物流合作
     *
     */
    public ShopLogistics addShopLogistics(ShopLogistics shopLogistics, Long shopId, UserDto user){

        return shopLogisticsDao.insert(shopLogistics, shopId, user);
    }

    /**
     * 查询商铺的物流合作
     *
     */
    public List<ShopLogistics> retrieveShopLogistics(Long shopId, Integer page, Integer pageSize){
        return shopLogisticsDao.retrieveByShopId(shopId, page, pageSize);
    }

    /**
     * 修改商铺的物流合作
     *
     */
    public void modifyShopLogistics(ShopLogistics bo, Long shopId, Long shopLogisticsId, UserDto user){
        // find原因：检查shopId与shopLogisticsId是否匹配
        ShopLogistics savedBo = shopLogisticsDao.findById(shopId, shopLogisticsId);
        shopLogisticsDao.save(bo, shopLogisticsId, user);
    }

    /**
     * 停用商铺的物流合作
     */
    public void modifyShopLogisticsValidity(Byte invalid, Long shopId, Long shopLogisticsId, UserDto user){
        // find原因：检查shopId与shopLogisticsId是否匹配
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(shopId, shopLogisticsId);
        shopLogistics.setValidity(invalid);
        shopLogisticsDao.save(shopLogistics, shopLogisticsId, user);
    }

    /**
     * 商铺指定无法配送地区
     *
     */
    public void addUndeliverableRegion(Undeliverable undeliverable, Long shopId, Long regionId, Long shopLogisticsId, UserDto user){
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(shopId, shopLogisticsId);
        undeliverableDao.insert(undeliverable, regionId, shopLogisticsId, user);
    }

    /**
     * 商铺修改无法配送地区
     *
     */
    public void modifyUndeliverableRegion(Undeliverable undeliverable, Long shopId, Long regionId, Long shopLogisticsId, UserDto user){
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(shopId, shopLogisticsId);
        Undeliverable savedBo = this.undeliverableDao.findByRegionIdAndShopLogisticsId(regionId, shopLogisticsId);
        undeliverable.setId(savedBo.getId());
        undeliverableDao.save(undeliverable, user);
    }

    /**
     * 商铺删除无法配送地区
     *
     */
    public void deleteUndeliverableRegion(Long shopId, Long regionId, Long shopLogisticsId, UserDto user){
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(shopId, shopLogisticsId);
        Undeliverable savedBo = this.undeliverableDao.findByRegionIdAndShopLogisticsId(regionId, shopLogisticsId);
        undeliverableDao.delete(savedBo.getId());
    }

    /**
     * 商铺查询无法配送地区
     *
     */
    public List<Undeliverable> retrieveUndeliverableRegion(Long shopId, Long shopLogisticsId, Integer page, Integer pageSize){
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(shopId, shopLogisticsId);
        return undeliverableDao.retrieveByShopLogisticsId(shopId, shopLogisticsId, page, pageSize);
    }
}
