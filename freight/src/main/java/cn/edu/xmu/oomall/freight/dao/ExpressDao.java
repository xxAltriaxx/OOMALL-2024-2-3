package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.mapper.jpa.ExpressPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.ExpressPo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;

/**
 * 2023-dgn3-009
 *
 * @author huangzian,fan ninghan
 */
@Repository
public class ExpressDao {
    private final static Logger logger = LoggerFactory.getLogger(ExpressDao.class);
    private ExpressPoMapper expressPoMapper;
    @Lazy
    private ShopLogisticsDao shopLogisticsDao;
    private LogisticsAdaptorFactory logisticsAdaptorFactory;

    @Autowired
    @Lazy
    public ExpressDao(ExpressPoMapper expressPoMapper, ShopLogisticsDao shopLogisticsDao, LogisticsAdaptorFactory logisticsAdaptorFactory) {
        this.expressPoMapper = expressPoMapper;
        this.shopLogisticsDao = shopLogisticsDao;
        this.logisticsAdaptorFactory = logisticsAdaptorFactory;
    }

    public Express insert(Express bo, UserDto user) {
        bo.setId(null);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        ExpressPo po = CloneFactory.copy(new ExpressPo(), bo);
        logger.debug("save: po = {}", po);
        po = expressPoMapper.save(po);
        bo.setId(po.getId());
        return bo;
    }

    public Express findById(Long shopId, Long id) throws RuntimeException{
        logger.debug("findById: id = {}", id);
        if (null == id) {
            throw new IllegalArgumentException("id can not be null");
        }
        Optional<ExpressPo> ret = expressPoMapper.findById(id);
        if (ret.isPresent()) {
            ExpressPo po = ret.get();
            logger.debug("findById: retrieve from database express = {}", po);
            if (!PLATFORM.equals(shopId) && !shopId.equals(po.getShopId())) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "物流单", id, shopId));
            }
            Express bo = CloneFactory.copy(new Express(), po);
            build(bo);
            return bo;
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "物流单", id));
        }
    }

    public void build(Express bo) throws RuntimeException{
        bo.setExpressDao(this);
        bo.setShopLogisticsDao(this.shopLogisticsDao);
        bo.setLogisticsAdaptor(this.logisticsAdaptorFactory);
    }

    public void save(Express bo, UserDto user) {
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        ExpressPo po = CloneFactory.copy(new ExpressPo(), bo);
        logger.debug("save: po = {}", po);
        ExpressPo updatePo = expressPoMapper.save(po);
        if (IDNOTEXIST.equals(updatePo.getId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "物流单", bo.getId()));
        }
    }

    public Express findByBillCode(Long shopId, String billCode) throws RuntimeException{
        ExpressPo po = this.expressPoMapper.findByBillCode(billCode);
        if (po != null) {
            logger.debug("findByBillCode: retrieve from database express = {}", po);
            Express bo = CloneFactory.copy(new Express(), po);
            if (!PLATFORM.equals(shopId) && !shopId.equals(po.getShopId())) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "物流单", po.getId(), shopId));
            }
            build(bo);
            return bo;
        } else{
            return null;
        }
    }

    public Long findShopLogisticsIdByBillCode(String billCode) throws RuntimeException {
        ExpressPo expressPo = expressPoMapper.findByBillCode(billCode);
        if (expressPo != null) {
            return expressPo.getShopLogisticsId();
        }
        else{
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }
}
