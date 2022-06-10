package com.cqp.iot.bootstrap;


import com.cqp.iot.properties.InitBean;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName BootstrapServer.java
 * @Description 启动类接口
 * @createTime 2022年05月16日 15:27:00
 */

public interface BootstrapServer {
    void shutdown();

    void setServerBean(InitBean serverBean);

    void start();
}
