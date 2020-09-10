/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.mp.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.api.BusinessException;
import co.yixiang.api.YshopException;
import co.yixiang.enums.*;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.domain.YxUserRecharge;
import co.yixiang.modules.user.service.YxUserRechargeService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.dto.WechatUserDto;
import co.yixiang.mp.config.WxPayConfiguration;
import co.yixiang.utils.IpUtil;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.ShopKeyUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @ClassName 微信支付WeixinPayService
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/27
 **/
@Service
@Slf4j
public class WeixinPayService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private YxUserService userService;
    @Autowired
    private YxStoreOrderService storeOrderService;
    @Autowired
    private YxUserRechargeService userRechargeService;


    /**
     * 统一支付入口
     * @param orderId 单号
     * @param from 来源
     * @param attach 备注 普通支付还是充值
     * @param body 内容
     * @return Object
     */
    public Object unifyPay(String orderId, String from, String attach, String body) {
        long uid = 0;
        int payPrice = 0;
        BigDecimal bigDecimal = new BigDecimal(100);
        if(BillDetailEnum.TYPE_3.getValue().equals(attach)){ //普通支付
            YxStoreOrderQueryVo orderInfo = storeOrderService.getOrderInfo(orderId,null);
            if(ObjectUtil.isNull(orderInfo)) throw new YshopException("订单不存在");
            if(orderInfo.getPaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())) {
                throw new YshopException("该订单已支付");
            }

            if(orderInfo.getPayPrice().compareTo(BigDecimal.ZERO) <= 0) throw new YshopException("该支付无需支付");

            uid = orderInfo.getUid().intValue();
            payPrice = bigDecimal.multiply(orderInfo.getPayPrice()).intValue();//计算分
        }else{ //充值
            YxUserRecharge userRecharge = userRechargeService.getOne(Wrappers.<YxUserRecharge>lambdaQuery()
                    .eq(YxUserRecharge::getOrderId,orderId));
            if(userRecharge == null) throw new BusinessException("充值订单不存在");

            if(userRecharge.getPaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())) {
                throw new YshopException("该订单已支付");
            }
            uid = userRecharge.getUid();
            payPrice = bigDecimal.multiply(userRecharge.getPrice()).intValue();
        }


        YxUser yxUser = userService.getById(uid);
        if(yxUser == null) throw new YshopException("用户错误");


        WechatUserDto wechatUserDto = yxUser.getWxProfile();

        WxPayService wxPayService = null;
        if(AppFromEnum.ROUNTINE.getValue().equals(from)){
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.WXAPP);
        }else if(AppFromEnum.APP.getValue().equals(from)){
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.APP);
        }else{
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.WECHAT);
        }
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setOutTradeNo(orderId);
        orderRequest.setTotalFee(payPrice);
        orderRequest.setSpbillCreateIp(IpUtil.getLocalIP());
        orderRequest.setNotifyUrl(this.getApiUrl() + "/api/wechat/notify");
        orderRequest.setBody(body);
        orderRequest.setAttach(attach);

        if(AppFromEnum.WEIXIN_H5.getValue().equals(from)){
            orderRequest.setTradeType("MWEB");
        }else if(AppFromEnum.APP.getValue().equals(from)){
            orderRequest.setTradeType("APP");
        } else{
            orderRequest.setTradeType("JSAPI");
            if(AppFromEnum.ROUNTINE.getValue().equals(from)){
                orderRequest.setOpenid(wechatUserDto.getRoutineOpenid());
            }else {
                orderRequest.setOpenid(wechatUserDto.getOpenid());
            }
        }
        try {
            return wxPayService.createOrder(orderRequest);
        }catch (WxPayException e) {
            log.info("支付错误信息：{}",e.getMessage());
            throw new BusinessException(e.getMessage());
        }


    }




    /**
     * 退款
     * @param orderId orderId
     * @param totalFee totalFee 单位分
     */
    public void refundOrder(String orderId, Integer totalFee) {

        YxStoreOrderQueryVo orderInfo = storeOrderService.getOrderInfo(orderId,null);
        if(PayTypeEnum.YUE.getValue().equals(orderInfo.getPayType())) return;

        WxPayService wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.WECHAT);
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();

        wxPayRefundRequest.setTotalFee(totalFee);//订单总金额
        wxPayRefundRequest.setOutTradeNo(orderId);
        wxPayRefundRequest.setOutRefundNo(orderId);
        wxPayRefundRequest.setRefundFee(totalFee);//退款金额
        wxPayRefundRequest.setNotifyUrl(this.getApiUrl() + "/api/notify/refund");

        try {
            wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            log.info("退款错误信息：{}",e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * 企业打款
     * @param openid 微信openid
     * @param no 单号
     * @param userName 用户姓名
     * @param amount 金额
     * @throws WxPayException
     */
    public void entPay(String openid,String no,String userName,Integer amount) throws WxPayException {
        WxPayService wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.WECHAT);
        EntPayRequest entPayRequest = new EntPayRequest();

        entPayRequest.setOpenid(openid);
        entPayRequest.setPartnerTradeNo(no);
        entPayRequest.setCheckName("FORCE_CHECK");
        entPayRequest.setReUserName(userName);
        entPayRequest.setAmount(amount);
        entPayRequest.setDescription("提现");
        entPayRequest.setSpbillCreateIp(IpUtil.getLocalIP());
        wxPayService.getEntPayService().entPay(entPayRequest);

    }


    /**
     * 返回H5 url
     * @return url
     */
    private String getApiUrl(){
        String apiUrl = redisUtils.getY(ShopKeyUtils.getApiUrl());
        if(StrUtil.isBlank(apiUrl)){
            throw new YshopException("请配置移动端api地址");
        }
        return apiUrl;
    }


}
