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
import co.yixiang.modules.heroConstruction.domain.ConstructionTypemedia;
import co.yixiang.modules.heroConstruction.service.ConstructionTypemediaService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypemediaQueryCriteria;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypemediaDto;
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
@Api(tags = "typeMedia管理")
@RestController
@RequestMapping("/api/constructionTypemedia")
public class ConstructionTypemediaController {

    private final ConstructionTypemediaService constructionTypemediaService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','constructionTypemedia:list')")
    public void download(HttpServletResponse response, ConstructionTypemediaQueryCriteria criteria) throws IOException {
        constructionTypemediaService.download(generator.convert(constructionTypemediaService.queryAll(criteria), ConstructionTypemediaDto.class), response);
    }

    @GetMapping
    @Log("查询typeMedia")
    @ApiOperation("查询typeMedia")
    @PreAuthorize("@el.check('admin','constructionTypemedia:list')")
    public ResponseEntity<Object> getConstructionTypemedias(ConstructionTypemediaQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(constructionTypemediaService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增typeMedia")
    @ApiOperation("新增typeMedia")
    @PreAuthorize("@el.check('admin','constructionTypemedia:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ConstructionTypemedia resources){
        return new ResponseEntity<>(constructionTypemediaService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改typeMedia")
    @ApiOperation("修改typeMedia")
    @PreAuthorize("@el.check('admin','constructionTypemedia:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ConstructionTypemedia resources){
        constructionTypemediaService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除typeMedia")
    @ApiOperation("删除typeMedia")
    @PreAuthorize("@el.check('admin','constructionTypemedia:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            constructionTypemediaService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
