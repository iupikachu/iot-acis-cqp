package com.cqp.iot.config;
import cn.hutool.core.lang.Assert;

import com.cqp.iot.auto.InitServer;
import com.cqp.iot.enums.ProtocolEnum;
import com.cqp.iot.properties.InitBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName ServerAutoConfig.java
 * @Description 自动化配置初始化服务
 * @createTime 2022年05月16日 20:51:00
 */

@Configuration
@ConditionalOnClass
@EnableConfigurationProperties({InitBean.class})
public class ServerAutoConfig {

    private static  final  int BACKLOG =   1024;

    private static final  int  CPU =Runtime.getRuntime().availableProcessors();

    private static final  int  SEDU_DAY =10;

    private static final  int TIMEOUT =120;

    private static final  int BUF_SIZE=10*1024*1024;

    @Bean(initMethod = "open", destroyMethod = "close")
    @ConditionalOnMissingBean
    public InitServer initServer(InitBean serverBean){
        Assert.notNull(serverBean.getPort(), serverBean.getServerName());

        if(serverBean.getBacklog() < 1){
            serverBean.setBacklog(BACKLOG);
        }
        if(serverBean.getBossThread() < 1){
            serverBean.setBossThread(CPU);
        }
        if(serverBean.getInitalDelay() <0 ){
            serverBean.setInitalDelay(SEDU_DAY);
        }
        if(serverBean.getPeriod() < 1){
            serverBean.setPeriod(SEDU_DAY);
        }
        if(serverBean.getHeart() < 1){
            serverBean.setHeart(TIMEOUT);
        }
        if(serverBean.getRevbuf() < 1){
            serverBean.setRevbuf(BUF_SIZE);
        }
        if(serverBean.getWorkThread() < 1){
            serverBean.setWorkThread(CPU * 2);
        }
        if(serverBean.getProtocol() == null){
            serverBean.setProtocol(ProtocolEnum.CUSTOM);
        }
        return new InitServer(serverBean);
    }
}
