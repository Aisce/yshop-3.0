/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.message.rocketmq;

import cn.hutool.core.util.ObjectUtil;

import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.modules.order.domain.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName 消费者
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/1/1
 **/
//@Component
//@RocketMQMessageListener(
//        topic = "yshop-topic",
//        consumerGroup = "yshop-group",
//        selectorExpression = "*"
//)
@Slf4j
@AllArgsConstructor
public class MqConsumer implements RocketMQListener<String> {

    private final YxStoreOrderService storeOrderService;

    @Override
    public void onMessage(String msg) {
        log.info("系统开始处理延时任务---订单超时未付款---订单id:" + msg);

        Long id = Long.valueOf(msg);

        YxStoreOrder order = storeOrderService.lambdaQuery()
                .eq(YxStoreOrder::getId, id)
                .eq(YxStoreOrder::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())
                .one();

        //只有待支付的订单能取消
        if(order != null){
            storeOrderService.cancelOrder(order.getOrderId(),null);
            log.info("订单id:{},未在规定时间支付取消成功",id);
        }

        log.info("=====处理成功======");

    }
}
