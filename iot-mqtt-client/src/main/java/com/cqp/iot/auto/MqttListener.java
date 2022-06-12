package com.cqp.iot.auto;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName MqttListener.java
 * @Description TODO
 * @createTime 2022年06月12日 15:40:00
 */
public interface MqttListener {

    void callBack(String topic,String msg);

    void callThrowable(Throwable e);


}
