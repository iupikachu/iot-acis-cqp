package com.cqp.iot.handler.customHandler;


import com.cqp.iot.Message.CustomRequest;
import com.cqp.iot.Message.CustomResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName CustomRequestHandler.java
 * @Description 自定义协议请求控制器
 * @createTime 2022年05月20日 09:36:00
 */
@Slf4j
public class CustomRequestHandler extends SimpleChannelInboundHandler<CustomRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomRequest customRequest) throws Exception {
      log.info("请求类型:{}", customRequest.getMessageType());
      log.info("数据:{}", customRequest.data);

        CustomResponse customResponse = new CustomResponse();
        customResponse.setResponse("服务端接收数据成功");
        ctx.writeAndFlush(customResponse);
    }
}
