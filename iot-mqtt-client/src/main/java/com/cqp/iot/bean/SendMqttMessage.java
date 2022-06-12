package com.cqp.iot.bean;

import com.cqp.iot.enums.ConfirmStatus;
import lombok.Builder;
import lombok.Data;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName SendMqttMessage.java
 * @Description Mqtt消息
 * @createTime 2022年06月12日 13:24:00
 */

@Data
@Builder
public class SendMqttMessage {
    /**
     * 主题
     */
    private String Topic;

    /**
     * 负载（数据）
     */
    private byte[] payload;

    /**
     * 服务质量
     */
    private int qos;

    /**
     * 是否为保留位
     */
    private boolean retained;

    /**
     * 重发标志
     */
    private boolean dup;

    /**
     * 报文标识符
     */
    private int messageId;

    private long timestamp;

    /**
     *  消息确认状态
     */
    private volatile ConfirmStatus confirmStatus;
}
