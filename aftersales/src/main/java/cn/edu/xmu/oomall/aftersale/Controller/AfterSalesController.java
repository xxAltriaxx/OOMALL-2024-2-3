package cn.edu.xmu.oomall.aftersale.Controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.aftersale.Service.AfterSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AfterSalesController {
    AfterSalesService afterSalesService;

    @Autowired
    AfterSalesController(AfterSalesService afterSalesService) {
        this.afterSalesService = afterSalesService;
    }

    @GetMapping("/aftersales/{id}")
    public ReturnObject createAfterSales(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/aftersales/{id}")
    public ReturnObject updateAfterSales(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/aftersales/{id}")
    public ReturnObject deleteAfterSales(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/")
    public ReturnObject getAllAfterSalesByShopId(@PathVariable Long id) {
        return null;
    }
}
