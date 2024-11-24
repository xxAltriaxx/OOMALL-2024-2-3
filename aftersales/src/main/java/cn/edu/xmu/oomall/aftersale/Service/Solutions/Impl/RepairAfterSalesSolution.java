package cn.edu.xmu.oomall.aftersale.Service.Solutions.Impl;

import cn.edu.xmu.oomall.aftersale.Service.Solutions.AfterSalesSolution;
import cn.edu.xmu.oomall.aftersale.Service.Solutions.AfterSalesSolutionFactory;
import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSalesHistory;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;

public class RepairAfterSalesSolution implements AfterSalesSolution {
    @Override
    public AfterSalesHistory solute(OrderItem orderItem, AfterSalesHistory afterSalesHistory){
        return afterSalesHistory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AfterSalesSolutionFactory.register("Repair",this);
    }
}
