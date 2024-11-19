package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.dao.WarehouseLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.mapper.po.WarehouseLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import jdk.jfr.Registered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public interface WarehouseLogisticsPoMapper extends JpaRepository<WarehouseLogisticsPo, Long> {
    List<WarehouseLogisticsPo> findAllByWarehouseIdEquals(Long warehouseId, Pageable pageable);

//    @Query(value = "select DISTINCT a from WarehouseLogisticsPo a join ShopLogisticsPo b on a.shopLogisticsId = b.id where a.warehouseId = :warehouseId and b.shopId = :shopId")
//    WarehouseLogisticsPo findByShopIdAndWarehouseId(Long shopId, Long warehouseId);

    WarehouseLogisticsPo findByShopLogisticsIdEqualsAndWarehouseIdEquals(Long shopLogisticsId, Long warehouseId);
}
