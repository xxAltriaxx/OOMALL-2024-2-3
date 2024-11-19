package cn.edu.xmu.oomall.ztoexpress.unit;

import cn.edu.xmu.oomall.ztoexpress.controller.vo.CancelPreOrderVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.CreateOrderVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.GetOrderInfoVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.QueryTrackVo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.ExpressBo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;

public final class CloneFactory {

    /**
     * Copy all fields from source to target
     *
     * @param target the target object
     * @param source the source object
     * @return the copied target object
     */
    public static OrderBo copy(OrderBo orderBo,CreateOrderVo createOrderVo){
        orderBo.getOrderPo().setPartnerType(createOrderVo.getPartnerType());
        orderBo.getOrderPo().setOrderType(createOrderVo.getOrderType());
        orderBo.getOrderPo().setPartnerOrderCode(createOrderVo.getPartnerOrderCode());
        orderBo.getSenderPo().setName(createOrderVo.getSenderInfo().getName());
        orderBo.getSenderPo().setPhone(createOrderVo.getSenderInfo().getPhone());
        orderBo.getSenderPo().setCity(createOrderVo.getSenderInfo().getCity());
        orderBo.getSenderPo().setDistrict(createOrderVo.getSenderInfo().getDistrict());
        orderBo.getSenderPo().setAddress(createOrderVo.getSenderInfo().getAddress());
        orderBo.getSenderPo().setProvince(createOrderVo.getSenderInfo().getProvince());
        orderBo.getReceiverPo().setName(createOrderVo.getReceiverInfo().getName());
        orderBo.getReceiverPo().setPhone(createOrderVo.getReceiverInfo().getPhone());
        orderBo.getReceiverPo().setCity(createOrderVo.getReceiverInfo().getCity());
        orderBo.getReceiverPo().setDistrict(createOrderVo.getReceiverInfo().getDistrict());
        orderBo.getReceiverPo().setAddress(createOrderVo.getReceiverInfo().getAddress());
        orderBo.getReceiverPo().setProvince(createOrderVo.getReceiverInfo().getProvince());
        return orderBo;
    }
    public static OrderBo copy(OrderBo orderBo,GetOrderInfoVo getOrderInfoVo){
        orderBo.getOrderPo().setPartnerType(getOrderInfoVo.getType());
        orderBo.getOrderPo().setOrderCode(getOrderInfoVo.getOrderCode());
        orderBo.getExpressPo().setBillcode(getOrderInfoVo.getBillCode());
        return orderBo;
    }
    public static OrderBo copy(OrderBo orderBo,CancelPreOrderVo cancelPreOrderVo){
        orderBo.setCancelType(cancelPreOrderVo.getCancelType());
        orderBo.getOrderPo().setOrderCode(cancelPreOrderVo.getOrderCode());
        orderBo.getExpressPo().setBillcode(cancelPreOrderVo.getBillCode());
        return orderBo;
    }
    public static ExpressBo copy(ExpressBo expressBo, QueryTrackVo queryTrackVo){
        expressBo.getExpressPo().setBillcode(queryTrackVo.getBillCode());
        return expressBo;
    }

}


