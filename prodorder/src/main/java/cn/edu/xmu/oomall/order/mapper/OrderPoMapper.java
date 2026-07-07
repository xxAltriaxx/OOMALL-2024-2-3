//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.mapper;

import cn.edu.xmu.oomall.order.mapper.po.OrderPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderPoMapper extends JpaRepository<OrderPo, Long> {

    long countByIdempotentKey(String idempotentKey);

    boolean existsByIdempotentKeyAndShopId(String idempotentKey, Long shopId);

    @Query("SELECT o.shopId FROM OrderPo o WHERE o.idempotentKey = :idempotentKey")
    Set<Long> findShopIdsByIdempotentKey(@Param("idempotentKey") String idempotentKey);
}
