//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.mapper;

import cn.edu.xmu.oomall.order.mapper.po.OrderIdempotentPo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderIdempotentPoMapper extends JpaRepository<OrderIdempotentPo, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrderIdempotentPo o WHERE o.idempotentKey = :idempotentKey")
    Optional<OrderIdempotentPo> findByIdForUpdate(@Param("idempotentKey") String idempotentKey);
}
