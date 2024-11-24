package cn.edu.xmu.oomall.aftersale.Controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.aftersale.Controller.vo.AfterSalesVo;
import cn.edu.xmu.oomall.aftersale.Service.AfterSalesService;
import cn.edu.xmu.oomall.aftersale.Service.bo.AfterSales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AfterSalesController {
    AfterSalesService afterSalesService;

    @Autowired
    AfterSalesController(AfterSalesService afterSalesService) {
        this.afterSalesService = afterSalesService;
    }

    /**
     *买家提交售后单
     */
    @PostMapping("/orderItems/{id}/aftersales")
    public ReturnObject createAfterSales(@PathVariable Long id,@RequestBody AfterSalesVo afterSalesVo) {
        if(afterSalesService.saveAfterSales(afterSalesVo)){
            return new ReturnObject(ReturnNo.OK);
        }
        return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
    }

    /**
     * 顾客查询所有售后单信息
     */
    @GetMapping("/aftersales")
    public ReturnObject findAllAfterSalesByUserId(@LoginUser Long userId) {
        return new ReturnObject(afterSalesService.findAllByUserId(userId));
    }

    /**
     * 顾客根据售后单id查询售后单信息
     */
    @GetMapping("/aftersales/{id}")
    public ReturnObject findById(@PathVariable Long id) {
        return new ReturnObject(afterSalesService.findById(id));
    }

    /**
     * 顾客修改售后单信息
     */
    @PutMapping("/aftersales/{id}")
    public ReturnObject updateAfterSales(@PathVariable Long id,@RequestBody AfterSalesVo afterSalesVo) {
        if(afterSalesService.saveAfterSales(afterSalesVo)){
            return new ReturnObject(ReturnNo.OK);
        }
        return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
    }

    /**
     * 顾客取消售后单
     */
    @DeleteMapping("/aftersales/{id}")
    public ReturnObject deleteById(@PathVariable Long id) {
        return new ReturnObject(afterSalesService.deleteById()?ReturnNo.OK:ReturnNo.INTERNAL_SERVER_ERR);
    }


}
