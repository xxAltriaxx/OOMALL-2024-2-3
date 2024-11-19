package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ExpressService {
    private static final Logger logger = LoggerFactory.getLogger(ExpressService.class);
    private ShopLogisticsDao shopLogisticsDao;
    private ExpressDao expressDao;

    @Autowired
    public ExpressService(ShopLogisticsDao shopLogisticsDao, ExpressDao expressDao) {
        this.shopLogisticsDao = shopLogisticsDao;
        this.expressDao = expressDao;
    }

    public Express createExpress(Long shopId, Express express, UserDto user) {
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(shopId, express.getShopLogisticsId());
        logger.debug("shopLogistics: shopLogistics = {}", shopLogistics);
        return shopLogistics.createExpress(shopId, express, user);
    }

    public Express findExpressById(Long shopId, Long id) {
        Express express = this.expressDao.findById(shopId, id);
        express.getNewStatus();
        return express;
    }

    public Express findExpressByBillCode(Long shopId, String billCode) {
        Express express = this.expressDao.findByBillCode(shopId, billCode);
        if (express != null) {
            express.getNewStatus();
        }
        return express;
    }

    public void sendExpress(Long shopId, Long id, UserDto user, LocalDateTime startTime, LocalDateTime endTime) {
        Express express = this.expressDao.findById(shopId, id);
        express.send(user,startTime,endTime);
    }

    public void cancelExpress(Long shopId, Long id, UserDto user) {
        Express express = this.expressDao.findById(shopId, id);
        express.cancel(user);
    }

    public void confirmExpress(Long shopId, Long id, Byte status, UserDto user) {
        Express express = this.expressDao.findById(shopId, id);
        express.confirm(status, user);
    }
}
