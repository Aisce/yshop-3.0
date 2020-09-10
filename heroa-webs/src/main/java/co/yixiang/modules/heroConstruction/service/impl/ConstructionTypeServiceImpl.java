/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.heroConstruction.service.impl;

import co.yixiang.modules.heroConstruction.domain.ConstructionType;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.heroConstruction.service.ConstructionTypeService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypeDto;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypeQueryCriteria;
import co.yixiang.modules.heroConstruction.service.mapper.ConstructionTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Jianzhuo
* @date 2020-09-01
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "constructionType")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConstructionTypeServiceImpl extends BaseServiceImpl<ConstructionTypeMapper, ConstructionType> implements ConstructionTypeService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(ConstructionTypeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<ConstructionType> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), ConstructionTypeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<ConstructionType> queryAll(ConstructionTypeQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(ConstructionType.class, criteria));
    }


    @Override
    public void download(List<ConstructionTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConstructionTypeDto constructionType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" typeName",  constructionType.getTypeName());
            map.put(" bedroomNum",  constructionType.getBedroomNum());
            map.put(" bathroomNum",  constructionType.getBathroomNum());
            map.put(" area",  constructionType.getArea());
            map.put(" typeDes",  constructionType.getTypeDes());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
