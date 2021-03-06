package com.cqp.iot.mqtt.scan;

import com.cqp.iot.bean.SendMqttMessage;
import com.cqp.iot.mqtt.producer.Producer;
import com.cqp.iot.pool.Scheduled;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName ScanScheduled.java
 * @Description 定时扫描消息确认
 * @createTime 2022年06月12日 19:12:00
 */

@Slf4j
@Data
public class ScanScheduled extends ScanRunnable{

    private Producer producer;

    private  ScheduledFuture<?> submit;

    private  int  seconds;

    public ScanScheduled(Producer producer, int seconds) {
        this.producer = producer;
        this.seconds = seconds;
    }

    public  void start(){
        Scheduled  scheduled = new ScheduledPool();
        this.submit = scheduled.submit(this);
    }

    public  void close(){
        if(submit != null && !submit.isCancelled()){
            submit.cancel(true);
        }
    }





    @Override
    public void doInfo(SendMqttMessage poll) {
        if(producer.getChannel().isActive()){
            if(checkTime(poll)){
                poll.setTimestamp(System.currentTimeMillis());
                switch (poll.getConfirmStatus()){
                    case PUB:
                        poll.setDup(true);
                        pubMessage(producer.getChannel(), poll);
                        break;
                    case PUBREC:
                        sendAck(MqttMessageType.PUBREC, true, producer.getChannel(), poll.getMessageId());
                        break;
                    case PUBREL:
                        sendAck(MqttMessageType.PUBREL, true, producer.getChannel(), poll.getMessageId());
                }
            }
        }else {
            log.info("channel is not alived");
            submit.cancel(true);
        }
    }

    private boolean checkTime(SendMqttMessage poll) {
        return System.currentTimeMillis() - poll.getTimestamp() >= seconds * 1000;
    }

    private class ScheduledPool implements Scheduled{
        private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        @Override
        public ScheduledFuture<?> submit(Runnable runnable) {
            return null;
        }
    }

}
