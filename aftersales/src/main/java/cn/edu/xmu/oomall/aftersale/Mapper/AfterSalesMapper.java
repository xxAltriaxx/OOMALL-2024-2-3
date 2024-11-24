package cn.edu.xmu.oomall.aftersale.Mapper;

import cn.edu.xmu.oomall.aftersale.Mapper.Po.AfterSalesPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AfterSalesMapper extends JpaRepository<AfterSalesPo,Long> {
    public AfterSalesPo save(AfterSalesPo afterSalesPo);
    public AfterSalesPo getById(Long id);
    public AfterSalesPo findByShopId(Long shopId);
    public List<AfterSalesPo> findAllByUserId(Long userId);
}
