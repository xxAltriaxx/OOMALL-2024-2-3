//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "promotion-service")
public interface PromotionDao {

    @GetMapping("/customers/{customerId}/coupons/{couponId}")
    InternalReturnObject<CouponDto> getCouponById(@PathVariable Long customerId, @PathVariable Long couponId);
}
