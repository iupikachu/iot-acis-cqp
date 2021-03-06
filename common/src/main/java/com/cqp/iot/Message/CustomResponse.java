package com.cqp.iot.Message;

import lombok.Data;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName CustomResponse.java
 * @Description 自定义协议消息请求
 * @createTime 2022年05月17日 10:05:00
 */
@Data
public class CustomResponse extends Message {
    @Override
    public int getMessageType() {
        return Message.CUSTOM_MESSAGE_TYPE_RESPONSE;
    }

    public String response;

}
