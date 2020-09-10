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
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.heroConstruction.domain.ConstructionProjecttype;
import co.yixiang.modules.heroConstruction.service.ConstructionProjecttypeService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjecttypeQueryCriteria;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjecttypeDto;
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
@Api(tags = "projectType管理")
@RestController
@RequestMapping("/api/constructionProjecttype")
public class ConstructionProjecttypeController {

    private final ConstructionProjecttypeService constructionProjecttypeService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','constructionProjecttype:list')")
    public void download(HttpServletResponse response, ConstructionProjecttypeQueryCriteria criteria) throws IOException {
        constructionProjecttypeService.download(generator.convert(constructionProjecttypeService.queryAll(criteria), ConstructionProjecttypeDto.class), response);
    }

    @GetMapping
    @Log("查询projectType")
    @ApiOperation("查询projectType")
    @PreAuthorize("@el.check('admin','constructionProjecttype:list')")
    public ResponseEntity<Object> getConstructionProjecttypes(ConstructionProjecttypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(constructionProjecttypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增projectType")
    @ApiOperation("新增projectType")
    @PreAuthorize("@el.check('admin','constructionProjecttype:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ConstructionProjecttype resources){
        return new ResponseEntity<>(constructionProjecttypeService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改projectType")
    @ApiOperation("修改projectType")
    @PreAuthorize("@el.check('admin','constructionProjecttype:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ConstructionProjecttype resources){
        constructionProjecttypeService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除projectType")
    @ApiOperation("删除projectType")
    @PreAuthorize("@el.check('admin','constructionProjecttype:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            constructionProjecttypeService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
