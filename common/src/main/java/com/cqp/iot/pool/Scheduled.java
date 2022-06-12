package com.cqp.iot.pool;

import java.util.concurrent.ScheduledFuture;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName Scheduled.java
 * @Description TODO
 * @createTime 2022年06月12日 19:08:00
 */

@FunctionalInterface
public interface Scheduled {

    ScheduledFuture<?> submit(Runnable runnable);

}
