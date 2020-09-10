/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.activity.domain.YxStoreCombination;
import co.yixiang.modules.activity.vo.StoreCombinationVo;
import co.yixiang.modules.activity.service.dto.YxStoreCombinationDto;
import co.yixiang.modules.activity.service.dto.YxStoreCombinationQueryCriteria;
import co.yixiang.modules.activity.vo.YxStoreCombinationQueryVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author hupeng
* @date 2020-05-13
*/
public interface YxStoreCombinationService  extends BaseService<YxStoreCombination>{



    /**
     * 减库存增加销量
     * @param num 数量
     * @param combinationId 拼团产品id
     */
    void decStockIncSales(int num,Long combinationId);

    /**
     * 增加库存 减少销量
     * @param num  数量
     * @param combinationId 拼团产品id
     */
    void incStockDecSales(int num,Long combinationId);

    YxStoreCombination getCombination(int id);

    //boolean judgeCombinationStock(Long combinationId,Integer cartNum);

    /**
     * 拼团列表
     * @param page page
     * @param limit limit
     * @return list
     */
    List<YxStoreCombinationQueryVo> getList(int page, int limit);

    /**
     * 获取拼团详情
     * @param id 拼团产品id
     * @param uid uid
     * @return StoreCombinationVo
     */
    StoreCombinationVo getDetail(Long id, Long uid);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxStoreCombinationQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxStoreCombinationDto>
    */
    List<YxStoreCombination> queryAll(YxStoreCombinationQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxStoreCombinationDto> all, HttpServletResponse response) throws IOException;

    /**
     * 修改状态
     * @param id 拼团产品id
     * @param status ShopCommonEnum
     */
    void onSale(Long id, Integer status);
}
