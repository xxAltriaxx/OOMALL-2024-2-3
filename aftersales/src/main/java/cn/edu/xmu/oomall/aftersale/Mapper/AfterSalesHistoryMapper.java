package cn.edu.xmu.oomall.aftersale.Mapper;

import cn.edu.xmu.oomall.aftersale.Mapper.Po.AfterSalesHistoryPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfterSalesHistoryMapper extends JpaRepository<AfterSalesHistoryPo,Long> {
    public AfterSalesHistoryPo save(AfterSalesHistoryPo afterSalesHistoryPo);
}
