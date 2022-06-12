package com.cqp.iot.mqtt.producer;

import com.cqp.iot.auto.MqttListener;
import com.cqp.iot.bean.SubMessage;
import com.cqp.iot.properties.ConnectOptions;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName Producer.java
 * @Description 生产者
 * @createTime 2022年06月12日 15:23:00
 */
public interface Producer {
    Channel getChannel();

    Producer connect(ConnectOptions connectOptions);

    void  close();

    void setMqttListener(MqttListener mqttListener);

    void pub(String topic,String message,boolean retained,int qos);

    void pub(String topic,String message);

    void pub(String topic,String message,int qos);

    void pub(String topic,String message,boolean retained);

    void sub(SubMessage... subMessages);

    void unsub(List<String> topics);

    void unsub();

    void disConnect();

}
