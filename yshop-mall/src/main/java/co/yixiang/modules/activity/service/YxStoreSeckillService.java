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
import co.yixiang.modules.activity.domain.YxStoreSeckill;
import co.yixiang.modules.activity.vo.StoreSeckillVo;
import co.yixiang.modules.activity.service.dto.YxStoreSeckillDto;
import co.yixiang.modules.activity.service.dto.YxStoreSeckillQueryCriteria;
import co.yixiang.modules.activity.vo.YxStoreSeckillQueryVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author hupeng
* @date 2020-05-13
*/
public interface YxStoreSeckillService  extends BaseService<YxStoreSeckill>{

    /**
     * 退回库存减少销量
     * @param num 数量
     * @param seckillId 秒杀产品id
     */
    void incStockDecSales(int num,Long seckillId);

    /**
     * 减库存增加销量
     * @param num 数量
     * @param seckillId 秒杀产品id
     */
    void decStockIncSales(int num,Long seckillId);

    //YxStoreSeckill getSeckill(int id);

    /**
     * 产品详情
     * @param id 砍价商品id
     * @return StoreSeckillVo
     */
    StoreSeckillVo getDetail(Long id);

    /**
     * 秒杀产品列表
     * @param page page
     * @param limit limit
     * @return list
     */
    List<YxStoreSeckillQueryVo> getList(int page, int limit, int time);


    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxStoreSeckillQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxStoreSeckillDto>
    */
    List<YxStoreSeckill> queryAll(YxStoreSeckillQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxStoreSeckillDto> all, HttpServletResponse response) throws IOException;
}
