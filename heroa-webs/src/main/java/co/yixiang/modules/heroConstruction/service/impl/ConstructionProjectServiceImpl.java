/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.heroConstruction.service.impl;

import co.yixiang.modules.heroConstruction.domain.*;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.heroConstruction.service.*;
import co.yixiang.modules.heroConstruction.vo.ProjectVo;
import co.yixiang.modules.heroConstruction.vo.ProjectsVo;
import co.yixiang.modules.heroConstruction.vo.TypeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectDto;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectQueryCriteria;
import co.yixiang.modules.heroConstruction.service.mapper.ConstructionProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Jianzhuo
* @date 2020-09-01
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "constructionProject")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConstructionProjectServiceImpl extends BaseServiceImpl<ConstructionProjectMapper, ConstructionProject> implements ConstructionProjectService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(ConstructionProjectQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<ConstructionProject> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), ConstructionProjectDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<ConstructionProject> queryAll(ConstructionProjectQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(ConstructionProject.class, criteria));
    }


    @Override
    public void download(List<ConstructionProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConstructionProjectDto constructionProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" projectName",  constructionProject.getProjectName());
            map.put(" projectLocation",  constructionProject.getProjectLocation());
            map.put(" projectBudget",  constructionProject.getProjectBudget());
            map.put(" projectArea",  constructionProject.getProjectArea());
            map.put(" projectType",  constructionProject.getProjectType());
            map.put(" projectCompleted",  constructionProject.getProjectCompleted());
            map.put(" projectShortdes",  constructionProject.getProjectShortdes());
            map.put(" projectDes",  constructionProject.getProjectDes());
            map.put(" projectBanner",  constructionProject.getProjectBanner());
            map.put(" projectBrochure",  constructionProject.getProjectBrochure());
            map.put(" projectOnhome",  constructionProject.getProjectOnhome());
            map.put(" projectCate",  constructionProject.getProjectCate());
            map.put(" projectSort",  constructionProject.getProjectSort());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    public ConstructionProjectmediaService cpm;

    @Override
    public List<ProjectsVo> getProjectList() {
        QueryWrapper<ConstructionProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("project_sort");
        List<ConstructionProject> projects = baseMapper.selectList(queryWrapper);
//        List<ProjectsVo> result = new ArrayList<>();
//        for (ConstructionProject project:
//                projects) {
//            QueryWrapper<ConstructionProjectmedia> qw = new QueryWrapper<>();
//            qw.eq("project_id",project.getId()).eq("sort",0);
//            ProjectsVo pv = new ProjectsVo();
//            pv.copy(project);
//            ConstructionProjectmedia media = cpm.getBaseMapper().selectOne(qw);
//            pv.setImgUrl(media != null ? media.getMediaUrl() : "null");
//            result.add(pv);
//        }
        List<ProjectsVo> result = fillPvo(projects);
        return result;
    }

    public List<ProjectsVo> fillPvo(List<ConstructionProject> projects){
        List<ProjectsVo> result = new ArrayList<>();
        for (ConstructionProject project:
                projects) {
            QueryWrapper<ConstructionProjectmedia> qw = new QueryWrapper<>();
            qw.eq("project_id",project.getId()).eq("sort",0);
            ProjectsVo pv = new ProjectsVo();
            pv.copy(project);
            ConstructionProjectmedia media = cpm.getBaseMapper().selectOne(qw);
            pv.setImgUrl(media != null ? media.getMediaUrl() : "null");
            result.add(pv);
        }
        return result;
    }

    public ConstructionTypeService ct;
    public ConstructionProjecttypeService cpt;
    public ConstructionTypemediaService ctm;

    @Override
    public ProjectVo getProjectDetail(String projectId) {
        //获取project基本数据
        ConstructionProject project = baseMapper.selectById(projectId);
        ProjectVo result = new ProjectVo();
        result.copy(project);
        result.setTypeVos(new ArrayList<>());

        //获取project的Media数据
        QueryWrapper<ConstructionProjectmedia> qw = new QueryWrapper<>();
        qw.eq("project_id", result.getId()).orderByAsc("sort");
        result.setProjectMedias(cpm.getBaseMapper().selectList(qw));

        //获取project的type数据
        QueryWrapper<ConstructionProjecttype> ptqw = new QueryWrapper<>();
        ptqw.eq("project_id", result.getId());
        ptqw.orderByAsc("sort");
        List<ConstructionProjecttype> cpts = cpt.getBaseMapper().selectList(ptqw);
        for (ConstructionProjecttype pt:
             cpts) {
            TypeVo tvo = new TypeVo();
            tvo.copy(ct.getById(pt.getTypeId()));
            //获取type媒体数据
            QueryWrapper<ConstructionTypemedia> tmqw = new QueryWrapper<>();
            tmqw.eq("type_id", tvo.getId()).orderByAsc("sort");
            tvo.setTypeMedias(ctm.getBaseMapper().selectList(tmqw));
            result.getTypeVos().add(tvo);
        }

        //获取relate project的数据
        QueryWrapper<ConstructionProject> cpqw = new QueryWrapper<>();
        cpqw.eq("project_cate", result.getProjectCate()).notIn("id", result.getId());
        List<ConstructionProject> relates = baseMapper.selectList(cpqw);
        if (relates.size() > 4) {
            Collections.shuffle(relates);
            relates = relates.subList(0, 4);
        }
        result.setRelateProjects(fillPvo(relates));
        return result;
    }
}
