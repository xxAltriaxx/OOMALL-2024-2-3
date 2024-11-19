package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.mapper.jpa.WarehouseLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.Shop;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.po.WarehouseLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public class WarehouseLogisticsDao {
    private final Logger logger = LoggerFactory.getLogger(WarehouseLogisticsDao.class);

    private ApplicationContext context;

    private WarehouseLogisticsPoMapper warehouseLogisticsPoMapper;

    private WarehouseDao warehouseDao;

    private ShopLogisticsDao shopLogisticsDao;

    @Autowired
    public WarehouseLogisticsDao(ApplicationContext context,
                                 WarehouseLogisticsPoMapper warehouseLogisticsPoMapper,
                                 ShopLogisticsDao shopLogisticsDao,
                                 WarehouseDao warehouseDao) {
        this.context = context;
        this.warehouseLogisticsPoMapper = warehouseLogisticsPoMapper;
        this.shopLogisticsDao = shopLogisticsDao;
        this.warehouseDao = warehouseDao;
    }

    public WarehouseLogistics build(WarehouseLogistics warehouseLogistics){
        warehouseLogistics.setWarehouseDao(this.warehouseDao);
        warehouseLogistics.setShopLogisticsDao(this.shopLogisticsDao);
        return warehouseLogistics;
    }

    /**
     * 新建仓库物流:
     * 检查warehouse存在，且与shopId对应（shop有权操作）
     * 检查shopLogistics存在且与shopId对应
     *
     * @param warehouseLogistics
     * @return
     */
    public void insert(WarehouseLogistics warehouseLogistics, Long shopId, Long shopLogisticsId, UserDto user) throws RuntimeException{
        warehouseLogistics.setShopLogisticsId(shopLogisticsId);
        warehouseLogistics.setCreator(user);
        // ensure not repetitive
        WarehouseLogistics savedWarehouseLogistics = this.findByShopLogisticsIdAndWarehouseId(warehouseLogistics.getShopLogisticsId(), warehouseLogistics.getWarehouseId());
        logger.debug("savedWarehouseLogistics = {}",savedWarehouseLogistics);
        if(savedWarehouseLogistics != null){
            logger.debug("repetitively added");
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSELOGISTIC_EXIST);
        }

        warehouseLogistics = this.build(warehouseLogistics);
        // check & build
        warehouseLogistics.buildWarehouse(shopId);
        warehouseLogistics.buildShopLogistics(shopId);
        // set
        warehouseLogistics.setGmtCreate(LocalDateTime.now());
        warehouseLogistics.setInvalid(WarehouseLogistics.VALID);

        logger.debug("saved = {}",warehouseLogistics);
        this.warehouseLogisticsPoMapper.save(CloneFactory.copy(new WarehouseLogisticsPo(), warehouseLogistics));
    }

    /**
     * 修改仓库物流：
     * 检查warehouse存在，且与shopId对应（shop有权操作）
     * 检查warehouseLogistics存在，且其对应的shopLogistics与shopId对应（shop有权操作）
     *
     * @param warehouseLogistics
     * @return
     */
    public void save(WarehouseLogistics warehouseLogistics, Long shopId, Long warehouseLogisticsId, UserDto user) throws RuntimeException{
        // set:
        warehouseLogistics.setId(warehouseLogisticsId);
        warehouseLogistics.setModifier(user);
        warehouseLogistics.setGmtModified(LocalDateTime.now());
        warehouseLogistics = this.build(warehouseLogistics);
        // check & build
        warehouseLogistics.buildWarehouse(shopId);
        warehouseLogistics.buildShopLogistics(shopId);

        this.warehouseLogisticsPoMapper.save(CloneFactory.copy(new WarehouseLogisticsPo(), warehouseLogistics));
    }

    /**
     * 删除仓库物流：
     * 由于shopId存在shopLogistics中，需要通过shopLogisticsId找到shopId，从而验证合法性
     */
    public void delete(WarehouseLogistics warehouseLogistics, Long shopId, Long warehouseId){
        warehouseLogistics = this.build(warehouseLogistics);
        warehouseLogistics.setWarehouseId(warehouseId);
        // check & build
        warehouseLogistics.buildWarehouse(shopId);
        warehouseLogistics.buildShopLogistics(shopId);

        this.warehouseLogisticsPoMapper.delete(CloneFactory.copy(new WarehouseLogisticsPo(), warehouseLogistics));
    }

    /**
     * 查找仓库物流
     * @param id
     * @return
     */
    public WarehouseLogistics findById(Long id) throws RuntimeException{
        Optional<WarehouseLogisticsPo> po = this.warehouseLogisticsPoMapper.findById(id);
        if (po.isPresent()) {
            return CloneFactory.copy(new WarehouseLogistics(), po.get());
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 查找仓库物流:
     * 检查warehouse存在，且其与shopId对应（shop有权操作）
     * 查找时不检查shopLogistics，因为shopLogisticsId为warehouseLogistics的属性，并非查找参数
     *
     * @param shopId
     * @param warehouseId
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<WarehouseLogistics> retrieveByShopIdAndWarehouseId(Long shopId, Long warehouseId, Integer page, Integer pageSize) throws RuntimeException {
        // check
        Warehouse warehouse = this.warehouseDao.findById(shopId, warehouseId);
        logger.debug("warehouse = {}", warehouse);

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return this.warehouseLogisticsPoMapper.findAllByWarehouseIdEquals(warehouseId, pageable).stream()
                .map(po -> {
                    WarehouseLogistics bo = CloneFactory.copy(new WarehouseLogistics(), po);
                    try {
                        bo = this.build(bo);        // 注入dao
                        bo.buildShopLogistics(shopId);    // 注入shopLogistics（在shopLogistic对象中注入logistics）
                        return bo;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }


    // 该函数用于在新增前检查，并未重复添加warehouseLogistics
    public WarehouseLogistics findByShopLogisticsIdAndWarehouseId(Long shopLogisticsId, Long warehouseId){
        WarehouseLogisticsPo po = this.warehouseLogisticsPoMapper.findByShopLogisticsIdEqualsAndWarehouseIdEquals(shopLogisticsId, warehouseId);
        if(po == null){
            return null;
        } else {
            return CloneFactory.copy(new WarehouseLogistics(), po);
        }
    }
}
