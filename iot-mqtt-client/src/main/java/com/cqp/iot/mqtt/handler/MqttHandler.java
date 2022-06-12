package com.cqp.iot.mqtt.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName MqttHandler.java
 * @Description 抽象mqtt协议处理器
 * @createTime 2022年06月12日 15:59:00
 */

@Slf4j
public abstract class MqttHandler extends SimpleChannelInboundHandler<MqttMessage> {
    MqttHandlerApi mqttHandlerApi;

    public MqttHandler(MqttHandlerApi mqttHandlerApi){
        this.mqttHandlerApi = mqttHandlerApi;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        Optional.ofNullable(mqttFixedHeader)
                .ifPresent(mqttFixedHeader1 -> doMessage(channelHandlerContext, mqttMessage));
    }

    public  abstract void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("【DefaultMqttHandler：channelInactive】"+ctx.channel().localAddress().toString()+"关闭成功");
        mqttHandlerApi.close(ctx.channel());
        //super.channelInactive(ctx); 有疑问
    }

    /**
     * 空闲检测
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            mqttHandlerApi.doTimeOut(ctx.channel(),(IdleStateEvent)evt);
        }
        super.userEventTriggered(ctx, evt);
    }
}
