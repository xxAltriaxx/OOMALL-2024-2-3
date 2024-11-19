package cn.edu.xmu.oomall.ztoexpress.service;

import cn.edu.xmu.oomall.ztoexpress.controller.vo.CancelPreOrderVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.CreateOrderVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.GetOrderInfoVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.QueryTrackVo;
import cn.edu.xmu.oomall.ztoexpress.dao.ExpressDao;
import cn.edu.xmu.oomall.ztoexpress.dao.OrderDao;
import cn.edu.xmu.oomall.ztoexpress.dao.PersonDao;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.ExpressBo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;

import cn.edu.xmu.oomall.ztoexpress.unit.CloneFactory;

import cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnResult;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ZTOExpressService {
    private OrderDao orderDao;
    private PersonDao personDao;
    private ExpressDao expressDao;
    @Autowired
    public ZTOExpressService(OrderDao orderDao, PersonDao personDao, ExpressDao expressDao) {
        this.orderDao = orderDao;
        this.personDao = personDao;
        this.expressDao = expressDao;
    }

    private Logger logger = LoggerFactory.getLogger(ZTOExpressService.class);
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult createOrder(CreateOrderVo createOrderVo) {
        OrderBo orderBo = CloneFactory.copy(new OrderBo(), createOrderVo);
        return orderDao.createOrder(orderBo);
    }
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult getOrderInfo(GetOrderInfoVo getOrderInfoVo) {
        OrderBo orderBo = new OrderBo();
        CloneFactory.copy(orderBo, getOrderInfoVo);
        return orderDao.getOrderInfo(orderBo);
    }
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult cancelPreOrder(CancelPreOrderVo cancelPreOrderVo) {
        OrderBo orderBo =new OrderBo();
        CloneFactory.copy(orderBo, cancelPreOrderVo);
        return orderDao.cancelPreOrder(orderBo);
    }
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult queryTrack(QueryTrackVo queryTrackVo) {
        ExpressBo expressBo=new ExpressBo();
        CloneFactory.copy(expressBo, queryTrackVo);
        return expressDao.queryTrack(expressBo);
    }
}
