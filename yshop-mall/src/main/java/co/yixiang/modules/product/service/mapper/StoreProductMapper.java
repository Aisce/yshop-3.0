/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.product.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.product.domain.YxStoreProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
* @author hupeng
* @date 2020-05-12
*/
@Repository
public interface StoreProductMapper extends CoreMapper<YxStoreProduct> {

    @Update("update yx_store_product set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{productId} and stock >= #{num}")
    int decStockIncSales(@Param("num") int num,@Param("productId") Long productId);

    @Update("update yx_store_product set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{productId}")
    int incStockDecSales(@Param("num") int num,@Param("productId") Long productId);

    @Update("update yx_store_product set sales=sales+#{num}" +
            " where id=#{productId}")
    int incSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set sales=sales-#{num}" +
            " where id=#{productId}")
    int decSales(@Param("num") int num,@Param("productId") Long productId);


    @Update("update yx_store_product set is_del = #{status} where id = #{id}")
    void updateDel(@Param("status")int status,@Param("id") Integer id);

    @Update("update yx_store_product set is_show = #{status} where id = #{id}")
    void updateOnsale(@Param("status") Integer status, @Param("id") Long id);
}
