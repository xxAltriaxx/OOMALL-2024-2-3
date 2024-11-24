package cn.edu.xmu.oomall.aftersale.Service.Solutions;

import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSalesHistory;
import org.springframework.beans.factory.InitializingBean;

public interface AfterSalesSolution extends InitializingBean {
    AfterSalesHistory solute(AfterSalesHistory afterSalesHistory);
}
