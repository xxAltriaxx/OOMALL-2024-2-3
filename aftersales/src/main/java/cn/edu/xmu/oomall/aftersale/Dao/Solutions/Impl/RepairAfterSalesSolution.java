package cn.edu.xmu.oomall.aftersale.Dao.Solutions.Impl;

import cn.edu.xmu.oomall.aftersale.Dao.Solutions.AfterSalesSolution;
import cn.edu.xmu.oomall.aftersale.Dao.Solutions.AfterSalesSolutionFactory;
import cn.edu.xmu.oomall.aftersale.Dao.bo.AfterSalesSoluteProgress;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;

public class RepairAfterSalesSolution implements AfterSalesSolution {
    @Override
    public AfterSalesSoluteProgress solute(OrderItem orderItem, AfterSalesSoluteProgress afterSalesSoluteProgress){
        return afterSalesSoluteProgress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AfterSalesSolutionFactory.register("Repair",this);
    }
}
