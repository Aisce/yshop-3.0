/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.rest;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.api.ApiResult;
import co.yixiang.api.YshopException;
import co.yixiang.common.aop.NoRepeatSubmit;
import co.yixiang.common.bean.LocalUser;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.modules.activity.domain.YxStoreBargainUser;
import co.yixiang.modules.activity.domain.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.param.BargainShareParam;
import co.yixiang.modules.activity.param.YxStoreBargainUserHelpQueryParam;
import co.yixiang.modules.activity.param.YxStoreBargainUserQueryParam;
import co.yixiang.modules.activity.service.YxStoreBargainService;
import co.yixiang.modules.activity.service.YxStoreBargainUserHelpService;
import co.yixiang.modules.activity.service.YxStoreBargainUserService;
import co.yixiang.modules.activity.vo.*;
import co.yixiang.modules.services.CreatShareProductService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 砍价 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */

@RestController
@RequestMapping
@Api(value = "砍价商品", tags = "营销:砍价商品", description = "砍价商品")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("unchecked")
public class StoreBargainController {

    private final YxStoreBargainService storeBargainService;
    private final YxStoreBargainUserService storeBargainUserService;
    private final YxStoreBargainUserHelpService storeBargainUserHelpService;
    private final YxUserService userService;
    private final YxSystemConfigService systemConfigService;
    private final CreatShareProductService creatShareProductService;

    @Value("${file.path}")
    private String path;




    /**
     * 砍价产品列表
     */
    @GetMapping("/bargain/list")
    @ApiOperation(value = "砍价产品列表",notes = "砍价产品列表")
    public ApiResult<Object> getYxStoreBargainPageList(@RequestParam(value = "page",defaultValue = "1") int page,
                                                       @RequestParam(value = "limit",defaultValue = "10") int limit){
        return ApiResult.ok(storeBargainService.getList(page, limit));
    }

    /**
    * 砍价详情
    */
    @AuthCheck
    @GetMapping("/bargain/detail/{id}")
    @ApiOperation(value = "砍价详情",notes = "砍价详情",response = YxStoreBargainQueryVo.class)
    public ApiResult<BargainVo> getYxStoreBargain(@PathVariable Long id){
        if(ObjectUtil.isNull(id)) throw new YshopException("参数错误");
        YxUser yxUser = LocalUser.getUser();
        return ApiResult.ok(storeBargainService.getDetail(id,yxUser));
    }

    /**
     * 砍价详情统计
     */
    @AuthCheck
    @PostMapping("/bargain/help/count")
    @ApiOperation(value = "砍价详情统计",notes = "砍价详情统计")
    public ApiResult<BargainCountVo> helpCount(@Validated @RequestBody YxStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        Long uid = LocalUser.getUser().getUid();
        return ApiResult.ok(storeBargainService.helpCount(bargainId,bargainUserUid,uid));
    }

    /**
     * 砍价顶部统计
     */
    @PostMapping("/bargain/share")
    @ApiOperation(value = "砍价顶部统计",notes = "砍价顶部统计")
    public ApiResult<TopCountVo> topCount(@Validated @RequestBody BargainShareParam param){
        Long bargainId = null;
        if(NumberUtil.isNumber(param.getBargainId())) {
            bargainId = Long.valueOf(param.getBargainId());
        }
        return ApiResult.ok(storeBargainService.topCount(bargainId));
    }

    /**
     * 参与砍价
     */
    @NoRepeatSubmit
    @AuthCheck
    @PostMapping("/bargain/start")
    @ApiOperation(value = "参与砍价",notes = "参与砍价")
    public ApiResult<Boolean> start(@Validated @RequestBody BargainShareParam param){
        Long bargainId = null;
        if(NumberUtil.isNumber(param.getBargainId())) {
            bargainId = Long.valueOf(param.getBargainId());
        }
        Long uid = LocalUser.getUser().getUid();
        storeBargainUserService.setBargain(bargainId,uid);
        return ApiResult.ok();
    }

    /**
     * 帮助好友砍价
     */
    @NoRepeatSubmit
    @AuthCheck
    @PostMapping("/bargain/help")
    @ApiOperation(value = "帮助好友砍价",notes = "帮助好友砍价")
    public ApiResult<Map<String,Object>> help(@Validated @RequestBody YxStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        Long uid = LocalUser.getUser().getUid();
        Map<String,Object> map = Maps.newHashMap();
        boolean isBargainUserHelp = storeBargainUserService
                .isBargainUserHelp(bargainId,bargainUserUid,uid);
        if(!isBargainUserHelp){
            map.put("status","SUCCESSFUL");
            return ApiResult.ok(map);
        }

        storeBargainService.doHelp(bargainId,bargainUserUid,uid);
        map.put("status","SUCCESS");
        return ApiResult.ok(map);
    }

    /**
     *  获取砍掉金额
     */
    @AuthCheck
    @PostMapping("/bargain/help/price")
    @ApiOperation(value = "获取砍掉金额",notes = "获取砍掉金额")
    public ApiResult<Map<String,Object>> price(@Validated @RequestBody YxStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        Long uid = LocalUser.getUser().getUid();
        Map<String,Object> map = Maps.newHashMap();
        YxStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,bargainUserUid);
        if(ObjectUtil.isNull(storeBargainUser)){
            map.put("price",0);
            return ApiResult.ok(map);
        }
        YxStoreBargainUserHelp storeBargainUserHelp = storeBargainUserHelpService
                .getOne(new QueryWrapper<YxStoreBargainUserHelp>()
                .lambda().eq(YxStoreBargainUserHelp::getBargainId,bargainId)
                .eq(YxStoreBargainUserHelp::getBargainUserId,storeBargainUser.getId())
                .eq(YxStoreBargainUserHelp::getUid,uid).last("limit 1"));
        if(ObjectUtil.isNull(storeBargainUserHelp)){
            map.put("price",0);
        }else{
            map.put("price",storeBargainUserHelp.getPrice());
        }
        return ApiResult.ok(map);
    }

    /**
     * 好友帮
     */
    @PostMapping("/bargain/help/list")
    @ApiOperation(value = "好友帮",notes = "好友帮")
    public ApiResult<List<YxStoreBargainUserHelpQueryVo>> helpList(@Validated @RequestBody YxStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        return ApiResult.ok(storeBargainUserHelpService.getList(bargainId,bargainUserUid
                ,param.getPage(),param.getLimit()));
    }

    /**
     * 获取开启砍价用户信息
     */
    @PostMapping("/bargain/start/user")
    @ApiOperation(value = "获取开启砍价用户信息",notes = "获取开启砍价用户信息")
    public ApiResult<Object> startUser(@Validated @RequestBody YxStoreBargainUserQueryParam param){
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        return ApiResult.ok(userService.getYxUserById(bargainUserUid));
    }

    /**
     * 砍价海报
     */
    @AuthCheck
    @PostMapping("/bargain/poster")
    @ApiOperation(value = "砍价海报",notes = "砍价海报")
    public ApiResult<Map<String,Object>> poster(@Validated @RequestBody BargainShareParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if(StrUtil.isBlank(siteUrl)){
            throw new YshopException("未配置h5地址");
        }
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if(StrUtil.isBlank(apiUrl)){
            throw new YshopException("未配置api地址");
        }
        YxUser userInfo = LocalUser.getUser();
        Map<String,Object> map = Maps.newHashMap();
        String url = creatShareProductService.getBargainPosterUrl(bargainId,userInfo,siteUrl,apiUrl,path);
        map.put("url",url);
        return ApiResult.ok(map);
    }


    /**
     * 砍价列表(已参与)
     */
    @AuthCheck
    @GetMapping("/bargain/user/list")
    @ApiOperation(value = "砍价列表(已参与)",notes = "砍价列表(已参与)")
    public ApiResult<List<YxStoreBargainUserQueryVo>> bargainUserList(
                                             @RequestParam(value = "page",defaultValue = "1") int page,
                                             @RequestParam(value = "limit",defaultValue = "10") int limit){
        Long uid = LocalUser.getUser().getUid();
        List<YxStoreBargainUserQueryVo> yxStoreBargainUserQueryVos = storeBargainUserService
                .bargainUserList(uid,page,limit);
        if(yxStoreBargainUserQueryVos.isEmpty()) throw new YshopException("暂无参与砍价");
        return ApiResult.ok(yxStoreBargainUserQueryVos);
    }

    /**
     * 砍价取消
     */
    @AuthCheck
    @PostMapping("/bargain/user/cancel")
    @ApiOperation(value = "砍价取消",notes = "砍价取消")
    public ApiResult<Boolean> bargainCancel(@Validated @RequestBody BargainShareParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long uid = LocalUser.getUser().getUid();
        storeBargainUserService.bargainCancel(bargainId,uid);
        return ApiResult.ok();
    }











}

