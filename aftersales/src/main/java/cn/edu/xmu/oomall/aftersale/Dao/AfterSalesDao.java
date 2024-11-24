package cn.edu.xmu.oomall.aftersale.Dao;

import cn.edu.xmu.oomall.aftersale.Mapper.AfterSalesMapper;
import cn.edu.xmu.oomall.aftersale.Mapper.Po.AfterSalesPo;
import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSales;
import cn.edu.xmu.oomall.aftersale.Service.dto.AfterSalesDto;
import cn.edu.xmu.oomall.aftersale.util.CloneFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AfterSalesDao {
    AfterSalesMapper afterSalesMapper;

    @Autowired
    AfterSalesDao(AfterSalesMapper afterSalesMapper) {
        this.afterSalesMapper = afterSalesMapper;
    }

    public boolean saveAfterSales(AfterSales afterSales) {
        try {
            afterSalesMapper.save(CloneFactory.clone(new AfterSalesPo(), afterSales));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<AfterSales> findAllByUserId(Long userId) {
        List<AfterSalesPo> list=afterSalesMapper.findAllByUserId(userId);
        return list.stream().map(o->CloneFactory.clone(new AfterSales(),o)).collect(Collectors.toList());
    }

    public List<AfterSales> findById(Long id) {
        Optional<AfterSalesPo> list=afterSalesMapper.findById(id);
        return list.stream().map(o->CloneFactory.clone(new AfterSales(),o)).collect(Collectors.toList());
    }
}
