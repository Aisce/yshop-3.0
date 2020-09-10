/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.activity.domain.YxStoreCombination;
import co.yixiang.modules.activity.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.product.vo.YxStoreProductQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author hupeng
* @date 2020-05-13
*/
@Repository
public interface YxStoreCombinationMapper extends CoreMapper<YxStoreCombination> {
    @Update("update yx_store_combination set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{combinationId} and stock >= #{num}")
    int decStockIncSales(@Param("num") int num, @Param("combinationId") Long combinationId);

    @Update("update yx_store_combination set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{combinationId}")
    int incStockDecSales(@Param("num") int num,@Param("combinationId") Long combinationId);

    @Select("SELECT c.id,c.effective_time as effectiveTime,c.image,c.people,c.price," +
            "c.title,c.unit_name as unitName,s.price as productPrice FROM yx_store_combination c " +
            "INNER JOIN yx_store_product s ON s.id=c.product_id " +
            " WHERE c.is_show = 1 AND c.is_del = 0 AND c.start_time < now() " +
            " AND c.stop_time > now() ORDER BY c.sort desc,c.id desc")
    List<YxStoreCombinationQueryVo> getCombList(Page page);

    @Select("SELECT c.id,c.effective_time as effectiveTime,c.image,c.people,c.price,c.browse," +
            "c.description,c.image,c.images,c.info,c.is_postage as isPostage,c.postage," +
            "c.product_id as productId,c.sales,c.start_time as startTime" +
            ",c.stock,c.stop_time stopTime," +
            "c.title,c.unit_name as unitName,s.price as productPrice FROM yx_store_combination c " +
            "INNER JOIN yx_store_product s ON s.id=c.product_id " +
            " WHERE c.is_show = 1 AND c.is_del = 0 AND c.id = #{id} ")
    YxStoreCombinationQueryVo getCombDetail(Long id);

    @Select("SELECT c.id,c.image,c.price,c.title as storeName,c.is_show as isShow,c.cost," +
            "c.is_postage as isPostage,c.postage,c.sales,c.stock,c.is_del as isDel" +
            " FROM yx_store_combination c " +
            " WHERE c.id = #{id} and c.is_del = 0 ")
    YxStoreProductQueryVo combinatiionInfo(Long id);
}
