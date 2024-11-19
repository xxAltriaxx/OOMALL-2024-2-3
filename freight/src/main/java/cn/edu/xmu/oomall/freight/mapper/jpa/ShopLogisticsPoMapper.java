package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public interface ShopLogisticsPoMapper extends JpaRepository<ShopLogisticsPo, Long> {
    public ShopLogisticsPo findByIdAndInvalidEquals(Long id, Byte invalid);

    public List<ShopLogisticsPo> findAllByShopIdOrderByPriorityAsc(Long shopId, Pageable pageable);

    public ShopLogisticsPo findByShopIdAndLogisticsId(Long shopId, Long logisticsId);

}
