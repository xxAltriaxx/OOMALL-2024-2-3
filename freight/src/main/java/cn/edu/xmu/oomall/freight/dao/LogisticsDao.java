package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.mapper.jpa.LogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.LogisticsPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Optional;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public class LogisticsDao {
    private final Logger logger = LoggerFactory.getLogger(LogisticsDao.class);

    private LogisticsPoMapper logisticsPoMapper;

    @Autowired
    public LogisticsDao(LogisticsPoMapper logisticsPoMapper) {
        this.logisticsPoMapper = logisticsPoMapper;
    }

    /**
     * 查询物流
     *
     */
    public Logistics findById(Long id) throws RuntimeException {
        Optional<LogisticsPo> po = logisticsPoMapper.findById(id);
        if (po.isPresent()) {
            return CloneFactory.copy(new Logistics(), po.get());
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }
}
