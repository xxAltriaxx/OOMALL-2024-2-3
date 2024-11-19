//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.template;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import cn.edu.xmu.oomall.shop.mapper.po.RegionTemplatePo;
import cn.edu.xmu.oomall.shop.mapper.WeightTemplatePoMapper;
import cn.edu.xmu.oomall.shop.mapper.po.WeightTemplatePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public class WeightTemplateDao implements AbstractTemplateDao {

    private static final Logger logger = LoggerFactory.getLogger(WeightTemplateDao.class);

    private WeightTemplatePoMapper mapper;

    @Autowired
    public WeightTemplateDao(WeightTemplatePoMapper weightTemplatePoMapper) {
        this.mapper = weightTemplatePoMapper;
    }

    @Override
    public RegionTemplate getRegionTemplate(RegionTemplatePo po) throws RuntimeException {
        WeightTemplate bo = CloneFactory.copy(new WeightTemplate(), po);
        Optional<WeightTemplatePo> wPo = this.mapper.findById(po.getObjectId());
        wPo.ifPresent(templatePo -> {
            CloneFactory.copy(bo, templatePo);
            bo.setObjectId(templatePo.getObjectId());
        });
        return bo;
    }

    @Override
    public void save(RegionTemplate bo) throws RuntimeException {
        Optional<WeightTemplatePo> ret = this.mapper.findById(bo.getObjectId());
        if(ret.isEmpty()){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA);
        }
        WeightTemplatePo savedPo = ret.get();
        WeightTemplatePo po = CloneFactory.copy(new WeightTemplatePo(), (WeightTemplate) bo);

        if(po.getFirstWeight()==null){
            po.setFirstWeight(savedPo.getFirstWeight());
        }
        if(po.getFirstWeightPrice()==null){
            po.setFirstWeightPrice(savedPo.getFirstWeightPrice());
        }
        if(po.getThresholds()==null){
            po.setThresholds(savedPo.getThresholds());
        }


        WeightTemplatePo retp = this.mapper.save(po);
        logger.debug("WeightTemplatePo:{}",retp);
    }

    @Override
    public void delete(String id) throws RuntimeException {
        this.mapper.deleteById(id);
    }

    @Override
    public String insert(RegionTemplate bo) throws RuntimeException {
        WeightTemplatePo po = CloneFactory.copy(new WeightTemplatePo(), (WeightTemplate)bo);
        WeightTemplatePo newPo = this.mapper.insert(po);
        return newPo.getObjectId();
    }

}
