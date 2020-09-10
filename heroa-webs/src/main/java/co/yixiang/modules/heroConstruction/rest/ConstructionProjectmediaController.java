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
import co.yixiang.modules.heroConstruction.domain.ConstructionProjectmedia;
import co.yixiang.modules.heroConstruction.service.ConstructionProjectmediaService;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectmediaQueryCriteria;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionProjectmediaDto;
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
@Api(tags = "projectMedia管理")
@RestController
@RequestMapping("/api/constructionProjectmedia")
public class ConstructionProjectmediaController {

    private final ConstructionProjectmediaService constructionProjectmediaService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','constructionProjectmedia:list')")
    public void download(HttpServletResponse response, ConstructionProjectmediaQueryCriteria criteria) throws IOException {
        constructionProjectmediaService.download(generator.convert(constructionProjectmediaService.queryAll(criteria), ConstructionProjectmediaDto.class), response);
    }

    @GetMapping
    @Log("查询projectMedia")
    @ApiOperation("查询projectMedia")
    @PreAuthorize("@el.check('admin','constructionProjectmedia:list')")
    public ResponseEntity<Object> getConstructionProjectmedias(ConstructionProjectmediaQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(constructionProjectmediaService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增projectMedia")
    @ApiOperation("新增projectMedia")
    @PreAuthorize("@el.check('admin','constructionProjectmedia:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ConstructionProjectmedia resources){
        return new ResponseEntity<>(constructionProjectmediaService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改projectMedia")
    @ApiOperation("修改projectMedia")
    @PreAuthorize("@el.check('admin','constructionProjectmedia:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ConstructionProjectmedia resources){
        constructionProjectmediaService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除projectMedia")
    @ApiOperation("删除projectMedia")
    @PreAuthorize("@el.check('admin','constructionProjectmedia:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            constructionProjectmediaService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
