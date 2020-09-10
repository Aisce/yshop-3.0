/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.impl;

import co.yixiang.api.YshopException;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.ShopCommonEnum;
import co.yixiang.modules.activity.domain.YxStoreCombination;
import co.yixiang.modules.activity.domain.YxStorePink;
import co.yixiang.modules.activity.domain.YxStoreVisit;
import co.yixiang.modules.activity.service.YxStoreCombinationService;
import co.yixiang.modules.activity.service.YxStorePinkService;
import co.yixiang.modules.activity.service.dto.PinkAllDto;
import co.yixiang.modules.activity.service.dto.YxStoreCombinationDto;
import co.yixiang.modules.activity.service.dto.YxStoreCombinationQueryCriteria;
import co.yixiang.modules.activity.service.mapper.YxStoreCombinationMapper;
import co.yixiang.modules.activity.service.mapper.YxStorePinkMapper;
import co.yixiang.modules.activity.service.mapper.YxStoreVisitMapper;
import co.yixiang.modules.activity.vo.StoreCombinationVo;
import co.yixiang.modules.activity.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.product.service.YxStoreProductReplyService;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;



/**
* @author hupeng
* @date 2020-05-13
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreCombinationServiceImpl extends BaseServiceImpl<YxStoreCombinationMapper, YxStoreCombination> implements YxStoreCombinationService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private YxStorePinkMapper yxStorePinkMapper;
    @Autowired
    private YxStoreVisitMapper yxStoreVisitMapper;

    @Autowired
    private YxStoreCombinationMapper yxStoreCombinationMapper;
    @Autowired
    private YxStoreProductReplyService replyService;
    @Autowired
    private YxStorePinkService storePinkService;


    /**
     * 减库存增加销量
     * @param num 数量
     * @param combinationId 拼团产品id
     */
    @Override
    public void decStockIncSales(int num, Long combinationId) {
        int res = yxStoreCombinationMapper.decStockIncSales(num,combinationId);
        if(res == 0) throw new YshopException("拼团产品库存不足");
    }

    /**
     * 增加库存 减少销量
     * @param num  数量
     * @param combinationId 拼团产品id
     */
    @Override
    public void incStockDecSales(int num, Long combinationId) {
        yxStoreCombinationMapper.incStockDecSales(num,combinationId);
    }

    @Override
    public YxStoreCombination getCombination(int id) {
        QueryWrapper<YxStoreCombination> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id).eq("is_del",0).eq("is_show",1);
        return yxStoreCombinationMapper.selectOne(wrapper);
    }

//    /**
//     * 判断库存是否足够
//     * @param combinationId 平团产品id
//     * @param cartNum 购物车数量
//     * @return boolean
//     */
//    @Override
//    public boolean judgeCombinationStock(Long combinationId, Integer cartNum) {
//        YxStoreCombination storeCombination = this.getById(combinationId);
//        return storeCombination.getStock() >= cartNum;
//    }

//    @Override
//    public YxStoreCombinationQueryVo getCombinationT(int id) {
//        return yxStoreCombinationMapper.getCombDetail(id);
//    }

    /**
     * 获取拼团详情
     * @param id 拼团产品id
     * @param uid uid
     * @return StoreCombinationVo
     */
    @Override
    public StoreCombinationVo getDetail(Long id, Long uid) {
        Date now = new Date();
        YxStoreCombination storeCombination = this
                .lambdaQuery().eq(YxStoreCombination::getId,id)
                .eq(YxStoreCombination::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .le(YxStoreCombination::getStartTime,now)
                .ge(YxStoreCombination::getStopTime,now)
                .one();
        if(storeCombination == null){
            throw new YshopException("拼团不存在或已下架");
        }

        YxStoreCombinationQueryVo storeCombinationQueryVo = generator.convert(storeCombination,
                YxStoreCombinationQueryVo.class);

        StoreCombinationVo storeCombinationVo = new StoreCombinationVo();

        storeCombinationVo.setStoreInfo(storeCombinationQueryVo);


        storeCombinationVo.setReply(replyService
                .getReply(storeCombinationQueryVo.getProductId()));
        int replyCount = replyService.productReplyCount(storeCombinationQueryVo.getProductId());
        storeCombinationVo.setReplyCount(replyCount);
        storeCombinationVo.setReplyChance(replyService.replyPer(storeCombinationQueryVo.getProductId()));

        PinkAllDto pinkAllDto = storePinkService.getPinkAll(id);
        storeCombinationVo.setPindAll(pinkAllDto.getPindAll());
        storeCombinationVo.setPink(pinkAllDto.getList());
        storeCombinationVo.setPinkOkList(storePinkService.getPinkOkList(uid));
        storeCombinationVo.setPinkOkSum(storePinkService.getPinkOkSumTotalNum());

        return storeCombinationVo;
    }

    /**
     * 拼团列表
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public List<YxStoreCombinationQueryVo> getList(int page, int limit) {
        Page<YxStoreCombination> pageModel = new Page<>(page, limit);
        return yxStoreCombinationMapper.getCombList(pageModel);
    }


    //=======================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreCombinationQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxStoreCombination> page = new PageInfo<>(queryAll(criteria));

        List<YxStoreCombinationDto> combinationDTOS = generator.convert(page.getList(),YxStoreCombinationDto.class);
        for (YxStoreCombinationDto combinationDTO : combinationDTOS) {
            //参与人数
            combinationDTO.setCountPeopleAll(yxStorePinkMapper.selectCount(new QueryWrapper<YxStorePink>().lambda()
                    .eq(YxStorePink::getCid,combinationDTO.getId())));

            //成团人数
            combinationDTO.setCountPeoplePink(yxStorePinkMapper.selectCount(new QueryWrapper<YxStorePink>().lambda()
                    .eq(YxStorePink::getCid,combinationDTO.getId())
                    .eq(YxStorePink::getKId,0)));//团长
            //获取查看拼团产品人数
            combinationDTO.setCountPeopleBrowse(yxStoreVisitMapper.selectCount(new QueryWrapper<YxStoreVisit>().lambda()
                    .eq(YxStoreVisit::getProductId,combinationDTO.getId())
                    .eq(YxStoreVisit::getProductType,"combination")));
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content",combinationDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxStoreCombination> queryAll(YxStoreCombinationQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreCombination.class, criteria));
    }


    @Override
    public void download(List<YxStoreCombinationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreCombinationDto yxStoreCombination : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商品id", yxStoreCombination.getProductId());
            map.put("推荐图", yxStoreCombination.getImage());
            map.put("轮播图", yxStoreCombination.getImages());
            map.put("活动标题", yxStoreCombination.getTitle());
            map.put("活动属性", yxStoreCombination.getAttr());
            map.put("参团人数", yxStoreCombination.getPeople());
            map.put("简介", yxStoreCombination.getInfo());
            map.put("价格", yxStoreCombination.getPrice());
            map.put("排序", yxStoreCombination.getSort());
            map.put("销量", yxStoreCombination.getSales());
            map.put("库存", yxStoreCombination.getStock());
            map.put("推荐", yxStoreCombination.getIsHost());
            map.put("产品状态", yxStoreCombination.getIsShow());
            map.put(" combination",  yxStoreCombination.getCombination());
            map.put("商户是否可用1可用0不可用", yxStoreCombination.getMerUse());
            map.put("是否包邮1是0否", yxStoreCombination.getIsPostage());
            map.put("邮费", yxStoreCombination.getPostage());
            map.put("拼团内容", yxStoreCombination.getDescription());
            map.put("拼团开始时间", yxStoreCombination.getStartTime());
            map.put("拼团结束时间", yxStoreCombination.getStopTime());
            map.put("拼团订单有效时间", yxStoreCombination.getEffectiveTime());
            map.put("拼图产品成本", yxStoreCombination.getCost());
            map.put("浏览量", yxStoreCombination.getBrowse());
            map.put("单位名", yxStoreCombination.getUnitName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 修改状态
     * @param id 拼团产品id
     * @param status ShopCommonEnum
     */
    @Override
    public void onSale(Long id, Integer status) {
        if(ShopCommonEnum.SHOW_1.getValue().equals(status)){
            status = ShopCommonEnum.SHOW_0.getValue();
        }else{
            status = ShopCommonEnum.SHOW_1.getValue();
        }
        YxStoreCombination yxStoreCombination = new YxStoreCombination();
        yxStoreCombination.setIsShow(status);
        yxStoreCombination.setId(id);
        this.saveOrUpdate(yxStoreCombination);
    }
}
