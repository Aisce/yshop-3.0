/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.heroConstruction.service.impl;

import co.yixiang.modules.heroConstruction.domain.ConstructionProjectmedia;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.heroConstruction.service.ConstructionProjectmediaService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectmediaDto;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectmediaQueryCriteria;
import co.yixiang.modules.heroConstruction.service.mapper.ConstructionProjectmediaMapper;
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
//@CacheConfig(cacheNames = "constructionProjectmedia")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConstructionProjectmediaServiceImpl extends BaseServiceImpl<ConstructionProjectmediaMapper, ConstructionProjectmedia> implements ConstructionProjectmediaService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(ConstructionProjectmediaQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<ConstructionProjectmedia> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), ConstructionProjectmediaDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<ConstructionProjectmedia> queryAll(ConstructionProjectmediaQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(ConstructionProjectmedia.class, criteria));
    }


    @Override
    public void download(List<ConstructionProjectmediaDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConstructionProjectmediaDto constructionProjectmedia : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" projectId",  constructionProjectmedia.getProjectId());
            map.put(" mediaUrl",  constructionProjectmedia.getMediaUrl());
            map.put(" mediaType",  constructionProjectmedia.getMediaType());
            map.put(" sort",  constructionProjectmedia.getSort());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
