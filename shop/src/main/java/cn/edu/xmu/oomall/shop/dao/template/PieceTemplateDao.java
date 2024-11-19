//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.template;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.shop.dao.bo.template.PieceTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.mapper.po.RegionTemplatePo;
import cn.edu.xmu.oomall.shop.mapper.PieceTemplatePoMapper;
import cn.edu.xmu.oomall.shop.mapper.po.PieceTemplatePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PieceTemplateDao implements AbstractTemplateDao {

    private static final Logger logger = LoggerFactory.getLogger(PieceTemplateDao.class);

    private PieceTemplatePoMapper mapper;

    @Autowired
    public PieceTemplateDao(PieceTemplatePoMapper pieceTemplatePoMapper) {
        this.mapper = pieceTemplatePoMapper;
    }

    @Override
    public RegionTemplate getRegionTemplate(RegionTemplatePo po) {
        PieceTemplate bo = CloneFactory.copy(new PieceTemplate(), po);
        Optional<PieceTemplatePo> wPo = this.mapper.findById(po.getObjectId());
        logger.debug("getRegionTemplate: wPo = {}, ObjectId = {}", wPo, po.getObjectId());
        wPo.ifPresent(templatePo -> {
            CloneFactory.copy(bo, templatePo);
            bo.setObjectId(templatePo.getObjectId());
            logger.debug("getRegionTemplate: templatePo = {}, bo = {}", templatePo, bo);
        });
        return bo;
    }

    @Override
    public void save(RegionTemplate bo) {

        Optional<PieceTemplatePo> ret = this.mapper.findById(bo.getObjectId());
        if(ret.isEmpty()){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA);
        }
        PieceTemplatePo savedPo = ret.get();
        PieceTemplatePo po = CloneFactory.copy(new PieceTemplatePo(), (PieceTemplate) bo);
        if(po.getAdditionalItems()==null){
            po.setAdditionalItems(savedPo.getAdditionalItems());
        }
        if(po.getAdditionalPrice()==null){
            po.setAdditionalPrice(savedPo.getAdditionalPrice());
        }
        if(po.getFirstItems()==null){
            po.setFirstItems(savedPo.getFirstItems());
        }
        if(po.getFirstPrice()==null){
            po.setFirstPrice(savedPo.getFirstPrice());
        }

        logger.debug("savedPo:{}",savedPo);

        PieceTemplatePo retp = this.mapper.save(po);
        logger.debug("PieceTemplatePo:{}",retp);
    }

    @Override
    public void delete(String id) throws RuntimeException {
        this.mapper.deleteById(id);
    }

    @Override
    public String insert(RegionTemplate bo) {
        PieceTemplatePo po = CloneFactory.copy(new PieceTemplatePo(), bo);
        PieceTemplatePo newPo = this.mapper.insert(po);
        return newPo.getObjectId();
    }
}
