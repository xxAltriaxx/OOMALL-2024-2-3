package cn.edu.xmu.oomall.ztoexpress.mapper;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.ExpressPo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressPoMapper extends JpaRepository<ExpressPo,Long>{
    ExpressPo findByBillcode(String billcode);
    List<ExpressPo> findAllByZtoOrderId(Long orderId);
}