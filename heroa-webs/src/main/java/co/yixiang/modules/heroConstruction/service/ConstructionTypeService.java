/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.heroConstruction.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.heroConstruction.domain.ConstructionType;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypeDto;
import co.yixiang.modules.heroConstruction.service.dto.ConstructionTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Jianzhuo
* @date 2020-09-01
*/
public interface ConstructionTypeService  extends BaseService<ConstructionType>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ConstructionTypeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ConstructionTypeDto>
    */
    List<ConstructionType> queryAll(ConstructionTypeQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ConstructionTypeDto> all, HttpServletResponse response) throws IOException;
}
