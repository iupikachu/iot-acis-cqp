package com.cqp.iot.bootstrap;

import com.cqp.iot.mqtt.handler.MqttHandler;
import com.cqp.iot.properties.ConnectOptions;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName AbstractBootstrapClient.java
 * @Description 抽象客户端启动类
 * @createTime 2022年06月12日 15:56:00
 */
public abstract class AbstractBootstrapClient implements BootstrapClient{
    /**
     * 添加 Mqtt 相关handler
     * @param channelPipeline
     * @param clientBean
     * @param mqttHandler
     */
    protected void initHandler(ChannelPipeline channelPipeline, ConnectOptions clientBean, MqttHandler mqttHandler){
        channelPipeline.addLast("decoder", new MqttDecoder());
        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
        channelPipeline.addLast(new IdleStateHandler(clientBean.getHeart(),0,0));
        channelPipeline.addLast(mqttHandler);
    }
}
