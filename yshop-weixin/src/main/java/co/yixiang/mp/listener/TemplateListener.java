/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.mp.listener;


import co.yixiang.enums.PayTypeEnum;
import co.yixiang.event.TemplateBean;
import co.yixiang.event.TemplateEvent;
import co.yixiang.event.TemplateListenEnum;
import co.yixiang.mp.service.WeixinPayService;
import co.yixiang.mp.service.WeixinTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author hupeng
 * 异步监听模板通知事件
 */
@Slf4j
@Component
public class TemplateListener implements SmartApplicationListener {

	@Autowired
	private WeixinTemplateService weixinTemplateService;
	@Autowired
	private WeixinPayService weixinPayService;

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
		return aClass == TemplateEvent.class;
	}

	@Async
	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		//转换事件类型
		TemplateEvent templateEvent = (TemplateEvent) applicationEvent;
		//获取注册用户对象信息
		TemplateBean templateBean = templateEvent.getTemplateBean();
		log.info("模板事件类型：{}"+templateBean.getTemplateType());
		switch (TemplateListenEnum.toType(templateBean.getTemplateType())){
			case TYPE_1:
				weixinTemplateService.paySuccessNotice(templateBean.getOrderId()
						,templateBean.getPrice(),templateBean.getUid());
				break;
			case TYPE_2:
				//处理退款与消息
				if(PayTypeEnum.WEIXIN.getValue().equals(templateBean.getPayType())){
					BigDecimal bigDecimal = new BigDecimal("100");
					int payPrice = bigDecimal.multiply(new BigDecimal(templateBean.getPrice())).intValue();
					weixinPayService.refundOrder(templateBean.getOrderId(),payPrice);
				}

				weixinTemplateService.refundSuccessNotice(templateBean.getOrderId(),templateBean.getPrice(),
						templateBean.getUid(),templateBean.getTime());
				break;
			case TYPE_3:
				weixinTemplateService.deliverySuccessNotice(templateBean.getOrderId(),templateBean.getDeliveryName(),
						templateBean.getDeliveryId(),templateBean.getUid());
				break;
			case TYPE_4:
				weixinTemplateService.rechargeSuccessNotice(templateBean.getTime(),templateBean.getPrice(),
						templateBean.getUid());
				break;
			default:
				//todo
		}


	}
}
