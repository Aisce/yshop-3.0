/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.rest;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.api.ApiResult;
import co.yixiang.api.YshopException;
import co.yixiang.common.bean.LocalUser;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.modules.activity.service.YxUserExtractService;
import co.yixiang.modules.services.CreatShareProductService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.param.PromParam;
import co.yixiang.modules.user.param.YxUserBillQueryParam;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.vo.YxUserBillQueryVo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserBillController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/10
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户分销", tags = "用户:用户分销", description = "用户分销")
public class UserBillController {

    private final YxUserBillService userBillService;
    private final YxUserExtractService extractService;
    private final YxSystemConfigService systemConfigService;
    private final YxUserService yxUserService;
    private final CreatShareProductService creatShareProductService;

    @Value("${file.path}")
    private String path;

    /**
     * 推广数据    昨天的佣金   累计提现金额  当前佣金
     */
    @AuthCheck
    @GetMapping("/commission")
    @ApiOperation(value = "推广数据",notes = "推广数据")
    public ApiResult<Map<String,Object>> commission(){
        YxUser yxUser = LocalUser.getUser();

        //昨天的佣金
        double lastDayCount = userBillService.yesterdayCommissionSum(yxUser.getUid());
        //累计提现金额
        double extractCount = extractService.extractSum(yxUser.getUid());

        Map<String,Object> map = Maps.newHashMap();
        map.put("lastDayCount",lastDayCount);
        map.put("extractCount",extractCount);
        map.put("commissionCount",yxUser.getBrokeragePrice());

        return ApiResult.ok(map);
    }

    /**
     * 积分记录
     */
    @AuthCheck
    @GetMapping("/integral/list")
    @ApiOperation(value = "积分记录",notes = "积分记录")
    public ApiResult<List<YxUserBillQueryVo>> userInfo(@RequestParam(value = "page",defaultValue = "1") int page,
                                                       @RequestParam(value = "limit",defaultValue = "10") int limit){
        Long uid = LocalUser.getUser().getUid();
        return ApiResult.ok(userBillService.userBillList(uid, BillDetailEnum.CATEGORY_2.getValue()
                ,page, limit));
    }


    /**
     * 分销二维码海报生成
     */
    @AuthCheck
    @GetMapping("/spread/banner")
    @ApiOperation(value = "分销二维码海报生成",notes = "分销二维码海报生成")
    public ApiResult<List<Map<String,Object>>> spreadBanner(@RequestParam(value = "",required=false) String form){
        YxUser yxUser = LocalUser.getUser();
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if(StrUtil.isEmpty(siteUrl)){
            throw new YshopException("未配置h5地址!");
        }
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if(StrUtil.isEmpty(apiUrl)){
            throw new YshopException("未配置api地址!");
        }

        String spreadUrl = creatShareProductService.getSpreadUrl(form,yxUser,siteUrl,apiUrl,path);

        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> map = Maps.newHashMap();
        map.put("id",1);
        map.put("pic","");
        map.put("title","分享海报");
        map.put("wap_poster",spreadUrl);
        list.add(map);
        return ApiResult.ok(list);
    }


    /**
     *  推广人统计
     */
    @AuthCheck
    @PostMapping("/spread/people")
    @ApiOperation(value = "推广人统计",notes = "推广人统计")
    public ApiResult<Map<String,Object>> spreadPeople(@Valid @RequestBody PromParam param){
        Long uid = LocalUser.getUser().getUid();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("list",yxUserService.getUserSpreadGrade(uid,param.getPage(),param.getLimit()
                ,param.getGrade(),param.getKeyword(),param.getSort()));

        Map<String,Integer> countMap = yxUserService.getSpreadCount(uid);
        map.put("total",countMap.get("first"));
        map.put("totalLevel",countMap.get("second"));
        return ApiResult.ok(map);
    }

    /**
     * 推广佣金明细
     * type  0 全部  1 消费  2 充值  3 返佣  4 提现
     * @return mixed
     */
    @AuthCheck
    @GetMapping("/spread/commission/{type}")
    @ApiOperation(value = "推广佣金明细",notes = "推广佣金明细")
    public ApiResult<Object> spreadCommission(@RequestParam(value = "page",defaultValue = "1") int page,
                                              @RequestParam(value = "limit",defaultValue = "10") int limit,
                                              @PathVariable String type){
        int newType = 0;
        if(NumberUtil.isNumber(type)) newType = Integer.valueOf(type);
        Long uid = LocalUser.getUser().getUid();
        return ApiResult.ok(userBillService.getUserBillList(page,limit,uid,newType));
    }


    /**
     * 推广订单
     */
    @AuthCheck
    @PostMapping("/spread/order")
    @ApiOperation(value = "推广订单",notes = "推广订单")
    public ApiResult<Object> spreadOrder(@RequestBody YxUserBillQueryParam param){
        Long uid = LocalUser.getUser().getUid();
        Map<String, Object> map = userBillService.spreadOrder(uid,param.getPage(),param.getLimit());
        return ApiResult.ok(map);
    }



}
