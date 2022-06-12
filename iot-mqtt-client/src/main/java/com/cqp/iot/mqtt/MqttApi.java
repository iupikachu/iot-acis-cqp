package com.cqp.iot.mqtt;

import com.cqp.iot.bean.SendMqttMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName MqttApi.java
 * @Description Mqtt协议API操作类
 * @createTime 2022年06月12日 11:06:00
 */

@Slf4j
public class MqttApi {
    // 在子类中使用
    protected ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    /**
     * 生产者发布消息 Publish
     * @param channel
     * @param mqttMessage
     */
    protected void pubMessage(Channel channel, SendMqttMessage mqttMessage){
        log.info("成功发送消息:"+new String(mqttMessage.getPayload()));
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, mqttMessage.isDup(), MqttQoS.valueOf(mqttMessage.getQos()), mqttMessage.isRetained(), 0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(mqttMessage.getTopic(), mqttMessage.getMessageId());
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader, mqttPublishVariableHeader, Unpooled.wrappedBuffer(mqttMessage.getPayload()));// 申请非池化内存，因为 publish 生产者用完即好，不需要复用
        channel.writeAndFlush(mqttPublishMessage);
    }

    /**
     * 消费者订阅消息 Subscribe
     * @param channel
     * @param mqttTopicSubscriptions
     * @param messageId 报文标识符
     *                  控制报文	    固定报头标志	  Bit 3	  Bit 2	  Bit 1	  Bit 0
     *                  SUBSCRIBE	 Reserved	    0	   0	   1	    0
     *                  部分类型MQTT控制报文的可变报头部分包含了2个字节的报文标识符字段（PUBLISH报文（当QoS>0时不需要））
     *                  MQTT控制报文 SUBSCRIBE 需要报文标识符（Packet Identifier）
     */
    protected void subMessage(Channel channel, List<MqttTopicSubscription> mqttTopicSubscriptions, int messageId){
        MqttSubscribePayload mqttSubscribePayload = new MqttSubscribePayload(mqttTopicSubscriptions);
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE,false, MqttQoS.AT_LEAST_ONCE,false,0);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader =MqttMessageIdVariableHeader.from(messageId); // 可变报头部分设置报文标识符字段
        MqttSubscribeMessage mqttSubscribeMessage = new MqttSubscribeMessage(mqttFixedHeader,mqttMessageIdVariableHeader,mqttSubscribePayload);
        channel.writeAndFlush(mqttSubscribeMessage);
    }

    /**
     * 各种确认消息
     * @param type
     * @param isDup
     * @param channel
     * @param messageId
     *
     *  qos1 : PUBACK PUBLISH报文的响应
     *  qos2 : PUBREC(发布收到)  PUBLISH报文的响应
     *         PUBREL(发布释放)  PUBREC报文的响应
     *         PUBCOMP(发布完成) PUBREL报文的响应
     *
     * PUBACK，PUBREC和 PUBREL 报文必须包含与最初发送的 PUBLISH 报文相同的报文标识符
     * SUBACK 和 UNSUBACK 必须包含在对应的 SUBSCRIBE 和 UNSUBSCRIBE 报文中使用的报文标识符
     *
     * CONNACK，PUBACK 可变报头有一个单字节的原因码
     * SUBACK和UNSUBACK报文的载荷字段包含一个或多个原因码
     *
     */
    protected void  sendAck(MqttMessageType type,boolean isDup,Channel channel, int messageId){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(type,isDup, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        MqttPubAckMessage mqttPubAckMessage = new MqttPubAckMessage(mqttFixedHeader,from);
        channel.writeAndFlush(mqttPubAckMessage);
    }

    /**
     * PubRec 发布收到
     * @param channel
     * @param messageId
     */
    protected void pubRecMessage(Channel channel,int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC,false, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        MqttMessage mqttPubAckMessage = new MqttMessage(mqttFixedHeader,from);
        channel.writeAndFlush(mqttPubAckMessage);
    }

    /**
     * 取消订阅
     * @param channel
     * @param topic
     * @param messageId
     */
    protected  void unSubMessage(Channel channel,List<String> topic,int messageId){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBSCRIBE,false, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttUnsubscribePayload MqttUnsubscribeMessage = new MqttUnsubscribePayload(topic);
        MqttUnsubscribeMessage mqttUnsubscribeMessage = new MqttUnsubscribeMessage(mqttFixedHeader,variableHeader,MqttUnsubscribeMessage);
        channel.writeAndFlush(mqttUnsubscribeMessage);
    }

    /**
     * 断开连接
     * @param channel
     */
    protected void sendDisConnect(Channel channel){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.DISCONNECT,false, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessage mqttMessage = new MqttMessage(mqttFixedHeader);
        channel.writeAndFlush(mqttMessage);
    }


}
