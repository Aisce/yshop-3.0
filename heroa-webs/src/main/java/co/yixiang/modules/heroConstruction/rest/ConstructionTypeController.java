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
import co.yixiang.modules.heroConstruction.domain.ConstructionType;
import co.yixiang.modules.heroConstruction.service.ConstructionTypeService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypeQueryCriteria;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypeDto;
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
@Api(tags = "type管理")
@RestController
@RequestMapping("/api/constructionType")
public class ConstructionTypeController {

    private final ConstructionTypeService constructionTypeService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','constructionType:list')")
    public void download(HttpServletResponse response, ConstructionTypeQueryCriteria criteria) throws IOException {
        constructionTypeService.download(generator.convert(constructionTypeService.queryAll(criteria), ConstructionTypeDto.class), response);
    }

    @GetMapping
    @Log("查询type")
    @ApiOperation("查询type")
    @PreAuthorize("@el.check('admin','constructionType:list')")
    public ResponseEntity<Object> getConstructionTypes(ConstructionTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(constructionTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增type")
    @ApiOperation("新增type")
    @PreAuthorize("@el.check('admin','constructionType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ConstructionType resources){
        return new ResponseEntity<>(constructionTypeService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改type")
    @ApiOperation("修改type")
    @PreAuthorize("@el.check('admin','constructionType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ConstructionType resources){
        constructionTypeService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除type")
    @ApiOperation("删除type")
    @PreAuthorize("@el.check('admin','constructionType:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            constructionTypeService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
