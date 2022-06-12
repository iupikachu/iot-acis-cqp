package com.cqp.iot.bootstrap;

import com.cqp.iot.handler.Codec.MessageCodecSharble;
import com.cqp.iot.handler.Codec.ProcotolFrameDecoder;
import com.cqp.iot.handler.customHandler.CustomRequestHandler;
import com.cqp.iot.properties.InitBean;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName AbstractBootstrapServer.java
 * @Description 抽象类 负责加载 编解码器 edec 协议 protocolHandler
 * @createTime 2022年05月16日 15:32:00
 */
public abstract class AbstractBootstrapServer implements BootstrapServer{

    protected void initHandler(ChannelPipeline channelPipeline, InitBean serverBean){
        // 判断是否使用 Ssl(待完成)

        initProtocolHandler(channelPipeline,serverBean);
        // 添加心跳handler  channelPipeline.addLast()
        // 添加channelPipeline.addLast(  SpringBeanUtils.getBean(serverBean.getMqttHander()));
    }

    private void initProtocolHandler(ChannelPipeline channelPipeline, InitBean serverBean){
        switch (serverBean.getProtocol()){
            case CUSTOM:
                // 日志
                LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
                // 自定义协议编解码器
                MessageCodecSharble messageCodec = new MessageCodecSharble();
                // 添加 CustomRequestHandler
                CustomRequestHandler customRequestHandler = new CustomRequestHandler();

                channelPipeline.addLast(loggingHandler);
                channelPipeline.addLast(messageCodec);
                channelPipeline.addLast(new ProcotolFrameDecoder());
                channelPipeline.addLast(customRequestHandler);
                break;
        }
    }
}

