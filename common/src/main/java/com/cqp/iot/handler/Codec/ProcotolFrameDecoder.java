package com.cqp.iot.handler.Codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName ProcotolFrameDecoder.java
 * @Description 协议定长帧拼接器
 *              在发送消息前，先约定用定长字节表示接下来数据的长度
 *              会将分开发送、接收的消息根据消息的长度进行自动拼接
 *              确保服务端收到的是一个完整的消息
 *              无法复用每一个 channel 对应一个自己的ProcotolFrameDecoder 所以不能加 @Sharable
 * @createTime 2022年05月17日 10:35:00
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProcotolFrameDecoder(){
        this(Integer.MAX_VALUE, 12, 4);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
}
