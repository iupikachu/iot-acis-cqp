package com.cqp.iot.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName Message.java
 * @Description 自定义协议消息
 * @createTime 2022年05月17日 09:59:00
 */
public abstract class Message implements Serializable {
    // 消息类型 用于在自定义协议中标识出消息类型
    public static final int CUSTOM_MESSAGE_TYPE_REQUEST = 1;
    public static final int CUSTOM_MESSAGE_TYPE_RESPONSE = 2;


    public Message() {}

    // 记录 rpc消息和标识id的对应关系
    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();
    static {
        messageClasses.put(CUSTOM_MESSAGE_TYPE_REQUEST, CustomRequest.class);
        messageClasses.put(CUSTOM_MESSAGE_TYPE_RESPONSE,CustomResponse.class);
    }

    /**
     * 根据消息类型，得到消息类
     * @param messageType
     * @return
     */
    public static Class<? extends Message> getMessageClassByType(int messageType){
        return messageClasses.get(messageType);
    }

    // 异步通信 序列号
    private int sequenceId;

    public Message(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    // 消息类型
    private int messageType;

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    // 由实现类自己返回自己的消息类型
    public abstract int getMessageType();



}
