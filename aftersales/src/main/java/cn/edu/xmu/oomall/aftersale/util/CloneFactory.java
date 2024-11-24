package cn.edu.xmu.oomall.aftersale.util;

import cn.edu.xmu.oomall.aftersale.Controller.vo.AfterSalesVo;
import cn.edu.xmu.oomall.aftersale.Mapper.Po.AfterSalesPo;
import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSales;
import cn.edu.xmu.oomall.aftersale.Service.dto.AfterSalesDto;

public class CloneFactory {
    public static AfterSales clone(AfterSales target, AfterSalesVo source) {
        target.setReason(source.getReason());
        target.setConclusion(source.getConclusion());
        target.setQuantity(source.getQuantity());
        target.setAddress(source.getAddress());
        target.setContact(source.getContact());
        target.setMobile(source.getMobile());
        target.setIn_arbitrated(0);
        target.setSerialNo(source.getSerialNo());
        target.setState(0);
        target.setType(source.getType());
        return target;
    }

    public static AfterSalesPo clone(AfterSalesPo target, AfterSales source) {
        target.setReason(source.getReason());
        target.setConclusion(source.getConclusion());
        target.setQuantity(source.getQuantity());
        target.setAddress(source.getAddress());
        target.setContact(source.getContact());
        target.setMobile(source.getMobile());
        target.setIn_arbitrated(source.getIn_arbitrated());
        target.setSerialNo(source.getSerialNo());
        target.setState(source.getState());
        target.setType(source.getType());
        return target;
    }

    public static AfterSales clone(AfterSales target, AfterSalesPo source) {
        target.setReason(source.getReason());
        target.setConclusion(source.getConclusion());
        target.setQuantity(source.getQuantity());
        target.setAddress(source.getAddress());
        target.setContact(source.getContact());
        target.setMobile(source.getMobile());
        target.setIn_arbitrated(source.getIn_arbitrated());
        target.setSerialNo(source.getSerialNo());
        target.setState(source.getState());
        target.setType(source.getType());
        return target;
    }

    public static AfterSalesDto clone(AfterSalesDto target, AfterSales source) {
        target.setReason(source.getReason());
        target.setConclusion(source.getConclusion());
        target.setQuantity(source.getQuantity());
        target.setAddress(source.getAddress());
        target.setContact(source.getContact());
        target.setMobile(source.getMobile());
        target.setIn_arbitrated(source.getIn_arbitrated());
        target.setSerialNo(source.getSerialNo());
        target.setState(source.getState());
        target.setType(source.getType());
        return target;
    }

}
