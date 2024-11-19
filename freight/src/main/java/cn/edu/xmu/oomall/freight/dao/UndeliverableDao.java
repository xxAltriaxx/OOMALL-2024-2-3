package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.jpa.UndeliverablePoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.UndeliverablePo;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public class UndeliverableDao {
    private final Logger logger = LoggerFactory.getLogger(UndeliverableDao.class);

    private ApplicationContext context;

    private UndeliverablePoMapper undeliverablePoMapper;

    private RegionDao regionDao;

    public UndeliverableDao(ApplicationContext context, UndeliverablePoMapper undeliverablePoMapper, RegionDao regionDao) {
        this.context = context;
        this.undeliverablePoMapper = undeliverablePoMapper;
        this.regionDao = regionDao;
    }

    public Undeliverable build(Undeliverable undeliverable){
        undeliverable.setRegionDao(this.regionDao);
        return undeliverable;
    }

    public void insert(Undeliverable undeliverable, Long regionId, Long shopLogisticsId, UserDto user) throws RuntimeException {
        undeliverable.setRegionId(regionId);
        undeliverable.setShopLogisticsId(shopLogisticsId);
        // ensure regionId is valid
        this.build(undeliverable);
        undeliverable.buildRegion();
        undeliverable.setCreator(user);
        undeliverable.setGmtCreate(LocalDateTime.now());

        UndeliverablePo po = CloneFactory.copy(new UndeliverablePo(), undeliverable);
        logger.debug("insert undeliverable = " + po);
        this.undeliverablePoMapper.save(po);
    }

    public void save(Undeliverable undeliverable,  UserDto user) throws RuntimeException {
        // ensure regionId is valid: 目前修改内容不包括region，不需要检查regionId
//        undeliverable.setRegionId(regionId);
//        this.build(undeliverable);
//        undeliverable.buildRegion();
        undeliverable.setModifier(user);
        undeliverable.setGmtModified(LocalDateTime.now());
        this.undeliverablePoMapper.save(CloneFactory.copy(new UndeliverablePo(), undeliverable));
    }

    public void delete(Long id) throws RuntimeException {
        this.undeliverablePoMapper.deleteById(id);
    }

    public List<Undeliverable> retrieveByShopLogisticsId(Long shopId, Long shopLogisticsId, Integer page, Integer pageSize) throws RuntimeException {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<UndeliverablePo> pos = this.undeliverablePoMapper.findAllByShopLogisticsId(shopLogisticsId, pageable);
        return this.undeliverablePoMapper.findAllByShopLogisticsId(shopLogisticsId, pageable).stream()
                .map(po -> {
                    Undeliverable bo = CloneFactory.copy(new Undeliverable(), po);
                    try {
                        bo = this.build(bo);
                        bo.buildRegion();
                        return bo;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public Undeliverable findByRegionIdAndShopLogisticsId(Long regionId, Long shopLogisticsId) {
        UndeliverablePo po = this.undeliverablePoMapper.findByRegionIdAndShopLogisticsId(regionId, shopLogisticsId);
        if (po != null)
            return CloneFactory.copy(new Undeliverable(), po);
        else
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
    }

}
