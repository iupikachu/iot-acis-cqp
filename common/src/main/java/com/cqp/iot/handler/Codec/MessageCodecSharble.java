package com.cqp.iot.handler.Codec;


import com.cqp.iot.Message.Message;
import com.cqp.iot.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName MessageCodecSharble.java
 * @Description 可共享自定义协议编解码器
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 * @createTime 2022年05月17日 10:24:00
 */

@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharble extends MessageToMessageCodec<ByteBuf, Message> {

    // 加密  Message -> bytebuf
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        // 4 字节魔数
        byteBuf.writeBytes(new byte[]{2,0,2,1});
        // 1 字节的版本号
        byteBuf.writeByte(1);
        // 1 字节的序列化方式 jdk:0 json:1
        byteBuf.writeByte(1);    // 暂时先写死
        // 1 字节的消息类型
        byteBuf.writeByte(msg.getMessageType());
        // 4 字节的序列号
            byteBuf.writeInt(msg.getSequenceId());
        // 1 字节的对齐填充无意义（作为扩展位）
        byteBuf.writeByte(0xff);

        // 序列化消息
        byte[] bytes = Serializer.json.serialize(msg);

        // 写入数据长度
        byteBuf.writeInt(bytes.length);

        // 写入序列化后的数据
        byteBuf.writeBytes(bytes);

        out.add(byteBuf);  // 添加到list，供后续的 handler 处理 ，ByteBuf 往后传递所以不需要 release ByteBuf
    }

    // 解密 bytebuf -> message
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int magicNum = msg.readInt();
        byte version = msg.readByte();
        byte serializon = msg.readByte(); // jdk:0 json: 1
        byte messageType = msg.readByte();  // rpc_request:1   rpc_response:2
        int sequenceId = msg.readInt();
        msg.readByte();
        int length = msg.readInt();
        byte[] data = new byte[length];
        msg.readBytes(data,0,length);

        // 反序列化
        Serializer serializer  = Serializer.values()[serializon];

        // 确定消息的具体类型
        Class<? extends Message> messageClass = Message.getMessageClassByType(messageType);
        Message message = serializer.deserialize(messageClass, data);

        log.debug("{},{},{},{},{},{},",magicNum,version,serializon,messageType,sequenceId,length);
        log.debug("{}",message);
        out.add(message);
    }
}
