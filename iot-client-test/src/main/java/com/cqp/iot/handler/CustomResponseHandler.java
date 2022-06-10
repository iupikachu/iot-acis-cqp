package com.cqp.iot.handler;


import com.cqp.iot.Message.CustomResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName CustomResponseHandler.java
 * @Description TODO
 * @createTime 2022年05月20日 10:25:00
 */

@Slf4j
public class CustomResponseHandler extends SimpleChannelInboundHandler<CustomResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomResponse msg) throws Exception {
        log.info("服务端相应消息:{}", msg.getResponse());
    }
}
