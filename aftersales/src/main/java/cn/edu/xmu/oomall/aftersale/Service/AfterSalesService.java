package cn.edu.xmu.oomall.aftersale.Service;

import cn.edu.xmu.oomall.aftersale.Controller.vo.AfterSalesVo;
import cn.edu.xmu.oomall.aftersale.Dao.AfterSalesDao;
import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSales;
import cn.edu.xmu.oomall.aftersale.Service.dto.AfterSalesDto;
import cn.edu.xmu.oomall.aftersale.util.CloneFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AfterSalesService {
    AfterSalesDao afterSalesDao;

    @Autowired
    public AfterSalesService(AfterSalesDao afterSalesDao){
        this.afterSalesDao = afterSalesDao;
    }

    public boolean createAfterSales(AfterSalesVo afterSalesVo){
        try{
            afterSalesDao.createAfterSales(CloneFactory.clone(new AfterSales(),afterSalesVo));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<AfterSalesDto> findAllByUserId(Long userId){
        try{
            List<AfterSales> list=afterSalesDao.findAllByUserId(userId);
            return list.stream().map(a->CloneFactory.clone(new AfterSalesDto(),a)).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
