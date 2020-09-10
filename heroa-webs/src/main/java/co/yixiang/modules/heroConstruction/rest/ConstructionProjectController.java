/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.heroConstruction.rest;
import java.util.Arrays;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.heroConstruction.domain.ConstructionProject;
import co.yixiang.modules.heroConstruction.service.ConstructionProjectService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectQueryCriteria;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Jianzhuo
* @date 2020-09-01
*/
@AllArgsConstructor
@Api(tags = "project管理")
@RestController
@RequestMapping("/api/constructionProject")
public class ConstructionProjectController {

    private final ConstructionProjectService constructionProjectService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','constructionProject:list')")
    public void download(HttpServletResponse response, ConstructionProjectQueryCriteria criteria) throws IOException {
        constructionProjectService.download(generator.convert(constructionProjectService.queryAll(criteria), ConstructionProjectDto.class), response);
    }

    @GetMapping
    @Log("查询project")
    @ApiOperation("查询project")
    @PreAuthorize("@el.check('admin','constructionProject:list')")
    public ResponseEntity<Object> getConstructionProjects(ConstructionProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(constructionProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    /**
     * 为projects页面返回project数据
     * @return
     */
    @GetMapping(value = "/get")
    @Log("为project页面返回所有project数据")
    @ApiOperation("为project页面返回所有project简要数据")
    @AnonymousAccess
    public ResponseEntity<Object> getProjects(){
        return new ResponseEntity<>(constructionProjectService.getProjectList(),HttpStatus.OK);
    }

    /**
     *
     * 为projectDetails页面返回数据
     * @return
     */
    @GetMapping(value = "/get/{projectId}")
    @Log("为projectDetails页面返回指定project详细数据")
    @ApiOperation("为projectDetails页面返回指定project详细数据")
    @AnonymousAccess
    public ResponseEntity<Object> getProject(@PathVariable String projectId){
        return new ResponseEntity<>(constructionProjectService.getProjectDetail(projectId),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增project")
    @ApiOperation("新增project")
    @PreAuthorize("@el.check('admin','constructionProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ConstructionProject resources){
        return new ResponseEntity<>(constructionProjectService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改project")
    @ApiOperation("修改project")
    @PreAuthorize("@el.check('admin','constructionProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ConstructionProject resources){
        constructionProjectService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除project")
    @ApiOperation("删除project")
    @PreAuthorize("@el.check('admin','constructionProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            constructionProjectService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
