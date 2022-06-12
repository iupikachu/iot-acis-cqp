package com.cqp.iot.mqtt.scan;

import com.cqp.iot.bean.SendMqttMessage;
import com.cqp.iot.enums.ConfirmStatus;
import com.cqp.iot.mqtt.MqttApi;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName ScanRunnable.java
 * @Description 扫描未确认信息，进行重发
 * @createTime 2022年06月12日 18:40:00
 */
@Slf4j
public abstract class ScanRunnable extends MqttApi implements Runnable{
    private ConcurrentLinkedQueue<SendMqttMessage> queue = new ConcurrentLinkedQueue<SendMqttMessage>();

    public  boolean addQueue(SendMqttMessage t){
        return queue.add(t);
    }

    public  boolean addQueues(List<SendMqttMessage> ts){
        return queue.addAll(ts);
    }


    /**
     * 有疑问对于这里的break
     */
    @Override
    public void run() {
        if(!queue.isEmpty()){
            SendMqttMessage poll;
            List<SendMqttMessage> list = new LinkedList<>();
            for(; (poll = queue.poll()) != null; ){
                if(poll.getConfirmStatus() != ConfirmStatus.COMPLETE){
                    list.add(poll);
                    doInfo(poll);
                }
                break;
            }
            addQueues(list);
        }
    }

    public  abstract  void  doInfo( SendMqttMessage poll);
}
