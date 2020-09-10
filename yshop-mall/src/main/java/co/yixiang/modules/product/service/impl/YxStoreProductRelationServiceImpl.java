/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.product.service.impl;

import co.yixiang.api.YshopException;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.product.domain.YxStoreProductRelation;
import co.yixiang.modules.product.service.YxStoreProductRelationService;
import co.yixiang.modules.product.service.mapper.YxStoreProductRelationMapper;
import co.yixiang.modules.product.vo.YxStoreProductRelationQueryVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 商品点赞和收藏表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductRelationServiceImpl extends BaseServiceImpl<YxStoreProductRelationMapper, YxStoreProductRelation> implements YxStoreProductRelationService {

    private final YxStoreProductRelationMapper yxStoreProductRelationMapper;


    /**
     * 获取用户收藏列表
     * @param page page
     * @param limit limit
     * @param uid 用户id
     * @return list
     */
    @Override
    public List<YxStoreProductRelationQueryVo> userCollectProduct(int page, int limit, Long uid) {
        Page<YxStoreProductRelation> pageModel = new Page<>(page, limit);
        List<YxStoreProductRelationQueryVo> list = yxStoreProductRelationMapper.selectList(pageModel,uid);
        return list;
    }

    /**
     * 添加收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    @Override
    public void addRroductRelation(long productId,long uid) {
        if(isProductRelation(productId,uid)) throw new YshopException("已收藏");
        YxStoreProductRelation storeProductRelation = YxStoreProductRelation.builder()
                .productId(productId)
                .uid(uid)
                .build();
        yxStoreProductRelationMapper.insert(storeProductRelation);
    }

    /**
     * 取消收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    @Override
    public void delRroductRelation(long productId,long uid) {
        YxStoreProductRelation productRelation = this.lambdaQuery()
                .eq(YxStoreProductRelation::getProductId,productId)
                .eq(YxStoreProductRelation::getUid,uid)
                .one();
        if(productRelation == null) throw new YshopException("已取消");


        this.removeById(productRelation.getId());
    }


    /**
     * 是否收藏
     * @param productId 商品ID
     * @param uid 用户ID
     * @return Boolean
     */
    @Override
    public Boolean isProductRelation(long productId, long uid) {
        int count = yxStoreProductRelationMapper
                .selectCount(Wrappers.<YxStoreProductRelation>lambdaQuery()
                        .eq(YxStoreProductRelation::getUid,uid)
                        .eq(YxStoreProductRelation::getProductId,productId));
        if(count > 0) return true;

        return false;
    }
}
