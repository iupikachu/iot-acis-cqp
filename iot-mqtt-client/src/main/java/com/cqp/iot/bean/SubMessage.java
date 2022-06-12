package com.cqp.iot.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName SubMessage.java
 * @Description 订阅消息
 * @createTime 2022年06月12日 15:47:00
 */

@Builder
@Data
public class SubMessage {
    private String topic;

    private MqttQoS qos;
}
