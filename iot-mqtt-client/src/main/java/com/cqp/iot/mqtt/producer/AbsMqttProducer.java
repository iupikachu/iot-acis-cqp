package com.cqp.iot.mqtt.producer;

import com.cqp.iot.mqtt.MqttApi;
import io.netty.channel.Channel;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName AbsMqttProducer.java
 * @Description 抽象Mqtt生产者
 * @createTime 2022年06月12日 15:49:00
 */
public abstract class AbsMqttProducer extends MqttApi implements Producer {

    protected Channel channel;


}
