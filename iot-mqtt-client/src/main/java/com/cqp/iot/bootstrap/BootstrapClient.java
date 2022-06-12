package com.cqp.iot.bootstrap;

import io.netty.channel.Channel;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName BootstrapClient.java
 * @Description 客户端启动类接口
 * @createTime 2022年06月12日 15:55:00
 */
public interface BootstrapClient {
    void shutdown();

    void initEventPool();

    Channel start();
}
