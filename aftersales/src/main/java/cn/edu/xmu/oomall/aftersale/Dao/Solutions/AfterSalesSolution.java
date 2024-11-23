package cn.edu.xmu.oomall.aftersale.Dao.Solutions;

import cn.edu.xmu.oomall.aftersale.Dao.bo.AfterSalesSoluteProgress;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import org.springframework.beans.factory.InitializingBean;

public interface AfterSalesSolution extends InitializingBean {
    AfterSalesSoluteProgress solute(OrderItem orderItem, AfterSalesSoluteProgress afterSalesSoluteProgress);
}
