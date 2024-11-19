package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.mapper.jpa.ShopLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * @author fan ninghan,huangzian
 * 2023-dng3-008,2023-dgn3-009
 */
@Repository
public class ShopLogisticsDao {
    private final Logger logger = LoggerFactory.getLogger(ShopLogisticsDao.class);

    private ShopLogisticsPoMapper shopLogisticsPoMapper;

    private LogisticsDao logisticsDao;

    @Autowired
    @Lazy
    public ShopLogisticsDao(RedisUtil redisUtil, ShopLogisticsPoMapper shopLogisticsPoMapper,
                            ExpressDao expressDao, LogisticsAdaptorFactory logisticsAdaptorFactory, LogisticsDao logisticsDao) {
        this.redisUtil = redisUtil;
        this.shopLogisticsPoMapper = shopLogisticsPoMapper;
        this.expressDao = expressDao;
        this.logisticsAdaptorFactory = logisticsAdaptorFactory;
        this.logisticsDao = logisticsDao;
    }

    public void build(ShopLogistics bo){
        bo.setExpressDao(this.expressDao);
        bo.setLogisticsDao(this.logisticsDao);
        bo.setLogisticsAdaptor(this.logisticsAdaptorFactory);
    }


    public ShopLogistics build(ShopLogisticsPo po, Optional<String> redisKey) throws RuntimeException{
        ShopLogistics bo = CloneFactory.copy(new ShopLogistics(), po);
        this.build(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    /**
     * 插入店铺物流
     *
     * @param shopLogistics
     * @param shopId
     * @param user
     *
     * @return
     */
    public ShopLogistics insert(ShopLogistics shopLogistics, Long shopId, UserDto user) throws RuntimeException {
        // ensure not repetitive
        ShopLogistics savedShopLogistics = this.findByShopIdAndLogisticsId(shopId, shopLogistics.getLogisticsId());
        if(savedShopLogistics != null) {
            throw new BusinessException(ReturnNo.FREIGHT_LOGISTIC_EXIST);
        }

        // check & build
        this.build(shopLogistics);
        shopLogistics.buildLogistics();

        shopLogistics.setShopId(shopId);
        shopLogistics.setInvalid(ShopLogistics.VALID);
        shopLogistics.setGmtCreate(LocalDateTime.now());
        shopLogistics.setGmtModified(null);
        shopLogistics.setCreator(user);

        ShopLogisticsPo savedPo = shopLogisticsPoMapper.save(CloneFactory.copy(new ShopLogisticsPo(), shopLogistics));
        shopLogistics.setId(savedPo.getId());
        return shopLogistics;
    }

    /**
     * 查询店铺物流
     *
     * @param shopId
     *
     * @return
     */
    public List<ShopLogistics> retrieveByShopId(Long shopId, Integer page, Integer pageSize) throws RuntimeException {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return this.shopLogisticsPoMapper.findAllByShopIdOrderByPriorityAsc(shopId, pageable).stream()
                .map(po -> {
                    ShopLogistics bo = CloneFactory.copy(new ShopLogistics(), po);
                    try {
                        this.build(bo);
                        bo.buildLogistics();
                        return bo;
                    }
                    catch (Exception e) { throw new RuntimeException(e); }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 修改店铺物流
     *
     * @param bo
     *
     * @return
     */
    public void save(ShopLogistics bo, Long id, UserDto user) throws RuntimeException {
        bo.setId(id);
        bo.setModifierId(user.getId());
        bo.setModifierName(user.getName());
        bo.setGmtModified(LocalDateTime.now());
        shopLogisticsPoMapper.save(CloneFactory.copy(new ShopLogisticsPo(), bo));

    }

    public ShopLogistics findByShopIdAndLogisticsId(Long shopId, Long logisticsId) throws RuntimeException {
        ShopLogisticsPo po = shopLogisticsPoMapper.findByShopIdAndLogisticsId(shopId, logisticsId);
        if (po==null) {
            return null;
        } else {
            return CloneFactory.copy(new ShopLogistics(),po);
        }
    }

    public static final String KEY = "SL%d";
    private RedisUtil redisUtil;
    @Lazy
    private ExpressDao expressDao;
    private LogisticsAdaptorFactory logisticsAdaptorFactory;

    @Value("${oomall.freight.shop-logistics.timeout}")
    private int timeout;

    public ShopLogistics findById(Long shopId, Long id) throws RuntimeException{
        if (null == id) {
            throw new IllegalArgumentException("ShopLogistics.findById: shopLogistic id is null");
        }
        logger.debug("findObjById: id = {}", id);
        String key = String.format(KEY, id);

        ShopLogistics shopLogistics = (ShopLogistics) redisUtil.get(key);
        if (shopLogistics != null) {
            this.build(shopLogistics);
            logger.debug("redis shopLogistics = {}", shopLogistics);
            if(!PLATFORM.equals(shopId) && !shopId.equals(shopLogistics.getShopId())){
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商铺物流", id, shopId));
            }
            return shopLogistics;
        } else {
            Optional<ShopLogisticsPo> po = this.shopLogisticsPoMapper.findById(id);
            if (po.isPresent()) {
                if (!PLATFORM.equals(shopId) && !shopId.equals(po.get().getShopId())) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商铺物流", id, shopId));
                }
                return this.build(po.get(), Optional.of(key));
            }
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺物流", id));
        }
    }


}
