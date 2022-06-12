package com.cqp.iot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author cqp
 * @version 1.0.0
 * @ClassName ConnectOptions.java
 * @Description 连接参数配置
 * @createTime 2022年06月12日 15:25:00
 */
//@ConfigurationProperties(prefix = "cqp.iot.client")
@Data
public class ConnectOptions {
    private long connectTime;

    private String serverIp;

    private int port ;

    private boolean keepalive ;

    private boolean reuseaddr ;

    private boolean tcpNodelay ;

    private int backlog ;

    private  int  sndbuf ;

    private int revbuf ;

    private int heart;


    private  int minPeriod ;

    private int bossThread;

    private int workThread;

    private MqttOpntions mqtt;

    @Data
    public static class MqttOpntions{

        private  String clientIdentifier;

        private  String willTopic;

        private  String willMessage;

        private  String userName;

        private  String password;

        private  boolean hasUserName;

        private  boolean hasPassword;

        private  boolean hasWillRetain;

        private  int willQos;

        private  boolean hasWillFlag;

        private  boolean hasCleanSession;

        private int KeepAliveTime;

    }
}
