package com.cqp.iot.auto;



import com.cqp.iot.bootstrap.NettyBootstrapServer;
import com.cqp.iot.properties.InitBean;


/**
 * @author cqp
 * @version 1.0.0
 * @ClassName InitServer.java
 * @Description 初始化服务
 * @createTime 2022年05月16日 15:21:00
 */
public class InitServer {

    private InitBean serverBean;

    public InitServer(InitBean serverBean) {
        this.serverBean = serverBean;
    }

    NettyBootstrapServer bootstrapServer;



    public void open(){
        if(serverBean != null){

            bootstrapServer = new NettyBootstrapServer();
            bootstrapServer.setServerBean(serverBean);
            bootstrapServer.start();
        }
    }

    public void close(){
        if(bootstrapServer != null){
            bootstrapServer.shutdown();
        }
    }
}
