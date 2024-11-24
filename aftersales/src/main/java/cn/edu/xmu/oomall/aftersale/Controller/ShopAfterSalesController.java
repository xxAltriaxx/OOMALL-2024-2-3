package cn.edu.xmu.oomall.aftersale.Controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.aftersale.Service.AfterSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShopAfterSalesController {
    AfterSalesService afterSalesService;

    @Autowired
    ShopAfterSalesController(AfterSalesService afterSalesService) {
        this.afterSalesService = afterSalesService;
    }

    /**
     * 商户查询所有售后单
     */
    @GetMapping("/shops/{id}/aftersales")
    public ReturnObject getAllAfterSalesByShopId(@PathVariable Long id) {
        return null;
    }

    /**
     * 商户根据售后单id查询售后单信息
     */
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    public ReturnObject getAfterSalesByShopIdAndId(@PathVariable Long shopId,@PathVariable Long id) {
        return null;
    }

    /**
     * 商家拒绝售后单
     */
    @DeleteMapping("/shops/{shopId}/aftersales/{id}")
    public ReturnObject shopCancelAfterSales(@PathVariable Long shopId,@PathVariable Long id) {
        return null;
    }

    /**
     * 商家同意售后单
     */
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    public ReturnObject confirmAfterSales(@PathVariable Long id) {
        return null;
    }

    /**
     * 店铺查询售后的服务单信息
     */
    @GetMapping("/shops/{shopId}/aftersales/{id}/services")
    public ReturnObject getAfterSalesServiceByShopIdAndId(@PathVariable Long id, @PathVariable Long shopId) {
        return null;
    }
}
