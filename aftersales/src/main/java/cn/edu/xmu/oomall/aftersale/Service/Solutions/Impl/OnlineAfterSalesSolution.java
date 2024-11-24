package cn.edu.xmu.oomall.aftersale.Service.Solutions.Impl;

import cn.edu.xmu.oomall.aftersale.Service.Solutions.AfterSalesSolution;
import cn.edu.xmu.oomall.aftersale.Service.Solutions.AfterSalesSolutionFactory;
import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSalesHistory;

public class OnlineAfterSalesSolution implements AfterSalesSolution {
    @Override
    public AfterSalesHistory solute(AfterSalesHistory afterSalesHistory){
        return afterSalesHistory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AfterSalesSolutionFactory.register("Online",this);
    }
}
