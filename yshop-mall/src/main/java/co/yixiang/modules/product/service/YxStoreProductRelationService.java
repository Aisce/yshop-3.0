/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.product.service;


import co.yixiang.common.service.BaseService;
import co.yixiang.modules.product.domain.YxStoreProductRelation;
import co.yixiang.modules.product.vo.YxStoreProductRelationQueryVo;

import java.util.List;

/**
 * <p>
 * 商品点赞和收藏表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
public interface YxStoreProductRelationService extends BaseService<YxStoreProductRelation> {

    /**
     * 是否收藏
     * @param productId 商品ID
     * @param uid 用户ID
     * @return Boolean
     */
    Boolean isProductRelation(long productId, long uid);

    /**
     *添加收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    void addRroductRelation(long productId,long uid);

    /**
     * 取消收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    void delRroductRelation(long productId,long uid);

    /**
     * 获取用户收藏列表
     * @param page page
     * @param limit limit
     * @param uid 用户id
     * @return list
     */
    List<YxStoreProductRelationQueryVo> userCollectProduct(int page, int limit, Long uid);




}
