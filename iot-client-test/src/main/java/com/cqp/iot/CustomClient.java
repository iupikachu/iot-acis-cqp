package com.cqp.iot;

import com.cqp.iot.Message.CustomRequest;
import com.cqp.iot.handler.Codec.MessageCodecSharble;
import com.cqp.iot.handler.Codec.ProcotolFrameDecoder;
import com.cqp.iot.handler.CustomResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * @author cqp
 * @version 1.0.0
 * @ClassName CustomClient.java
 * @Description 客户端测试连接
 * @createTime 2022年06月09日 13:44:00
 */
@Slf4j
public class CustomClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        //LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharble MESSAGE_CODEC = new MessageCodecSharble();
        CustomResponseHandler customResponseHandler = new CustomResponseHandler();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast("CustomClient handler",new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            CustomRequest customRequest = new CustomRequest();
                            customRequest.setData("物联网测试数据：0.1℃/0.2℉");
                            ctx.writeAndFlush(customRequest);
                            System.out.println("客户端发送数据:" + customRequest.data);
                            //super.channelActive(ctx);
                        }
                    });
                    ch.pipeline().addLast(customResponseHandler);
                }
            });

            Channel channel = bootstrap.connect("localhost", 1884).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CustomClient error",e);
        }finally {
            group.shutdownGracefully();
        }
    }
}
