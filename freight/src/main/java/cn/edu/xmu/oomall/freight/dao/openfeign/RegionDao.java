package cn.edu.xmu.oomall.freight.dao.openfeign;
//School of Informatics Xiamen University, GPL-3.0 license

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@Repository
public class RegionDao {

    private RegionMapper regionMapper;

    @Autowired
    public RegionDao(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    private Region build(RegionPo po){
        Region bo = CloneFactory.copy(new Region(), po);
        bo.setRegionDao(this);
        return bo;
    }

    public Region findRegionById(Long id){
        InternalReturnObject<RegionPo> ret = this.regionMapper.findRegionById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return this.build(ret.getData());
        }
    }

    public List<Region> retrieveParentRegionsById(Long regionId){
        InternalReturnObject<List<RegionPo>> ret= this.regionMapper.retrieveParentRegionsById(regionId);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData().stream().map(po -> this.build(po)).collect(Collectors.toList());
        }
    }

    public Long retrieveRegionIdByName(String name){
        InternalReturnObject<RegionPo> ret=this.regionMapper.retriveRegionIdByName(name);
        ReturnNo returnNo=ReturnNo.getByCode(ret.getErrno());
        if(!returnNo.equals(ReturnNo.OK)){
            throw new BusinessException(returnNo,ret.getErrmsg());
        }else{
            return ret.getData().getId();
        }
    }
}
