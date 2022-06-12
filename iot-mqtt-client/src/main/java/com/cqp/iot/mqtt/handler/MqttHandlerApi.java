package com.cqp.iot.mqtt.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName MqttHandlerApi.java
 * @Description 自定义 对外暴露 消息处理api
 * @createTime 2022年06月12日 16:22:00
 */
public interface MqttHandlerApi {
    void close(Channel channel);

    void puback(Channel channel, MqttMessage mqttMessage);

    void pubrec(Channel channel, MqttMessage mqttMessage);

    void pubrel(Channel channel, MqttMessage mqttMessage);

    void pubcomp(Channel channel, MqttMessage mqttMessage);

    void doTimeOut(Channel channel, IdleStateEvent evt);
}
