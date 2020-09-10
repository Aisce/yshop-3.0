/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.message.rocketmq;

import co.yixiang.exception.ErrorRequestException;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName 生成者
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/1/1
 **/
//@Component
@AllArgsConstructor
public class MqProducer {
     //注入rocketMQ的模板
    private final RocketMQTemplate rocketMQTemplate;


    /**
     * 发送延时消息10分钟
     *
     * @param topic 主题
     * @param msg 消息
     */
    public void sendMsg(String topic, String msg) {
        DefaultMQProducer defaultMQProducer = rocketMQTemplate.getProducer();

        Message message = new Message(topic,msg.getBytes());
        message.setDelayTimeLevel(14);

        try {
            defaultMQProducer.send(message);
        } catch (MQClientException e) {
            throw new ErrorRequestException("RocketMQ服务没启动哦");
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
