package cn.edu.xmu.oomall.aftersale.Service.Solutions;

import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSalesHistory;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import org.springframework.beans.factory.InitializingBean;

public interface AfterSalesSolution extends InitializingBean {
    AfterSalesHistory solute(OrderItem orderItem, AfterSalesHistory afterSalesHistory);
}
