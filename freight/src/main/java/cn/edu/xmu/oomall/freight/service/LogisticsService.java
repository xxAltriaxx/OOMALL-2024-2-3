package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.LogisticsDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LogisticsService {
    private ExpressDao expressDao;

    private ShopLogisticsDao shopLogisticsDao;

    private LogisticsDao logisticsDao;

    @Autowired
    public LogisticsService(ExpressDao expressDao, ShopLogisticsDao shopLogisticsDao, LogisticsDao logisticsDao) {
        this.expressDao = expressDao;
        this.shopLogisticsDao = shopLogisticsDao;
        this.logisticsDao = logisticsDao;
    }

    // 此处面向功能
    public Logistics findByBillCode(String billCode){
        Long shopLogisticsId = this.expressDao.findShopLogisticsIdByBillCode(billCode);
        ShopLogistics shopLogistics = this.shopLogisticsDao.findById(PLATFORM, shopLogisticsId);
        return this.logisticsDao.findById(shopLogistics.getLogisticsId());
    }
}
