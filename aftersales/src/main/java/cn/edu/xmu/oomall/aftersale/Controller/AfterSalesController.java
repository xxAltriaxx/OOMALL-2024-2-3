package cn.edu.xmu.oomall.aftersale.Controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
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
    public ReturnObject submitAfterSales(@PathVariable Long id) {
        return null;
    }

    /**
     * 顾客查询所有售后单信息
     */
    @GetMapping("/aftersales")
    public ReturnObject getAllAfterSales(@PathVariable Long id) {
        return null;
    }

    /**
     * 顾客根据售后单id查询售后单信息
     */
    @GetMapping("/aftersales/{id}")
    public ReturnObject getAfterSalesById(@PathVariable Long id) {
        return null;
    }

    /**
     * 顾客修改售后单信息
     */
    @PutMapping("/aftersales/{id}")
    public ReturnObject updateAfterSales(@PathVariable Long id) {
        return null;
    }

    /**
     * 顾客取消售后单
     */
    @DeleteMapping("/aftersales/{id}")
    public ReturnObject deleteAfterSales(@PathVariable Long id) {
        return null;
    }


}
