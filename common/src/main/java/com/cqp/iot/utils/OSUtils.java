package com.cqp.iot.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName OSUtils.java
 * @Description 获取操作系统类
 * @createTime 2022年05月16日 16:30:00
 */

@Slf4j
public class OSUtils {
    public static final String OS_NAME = System.getProperty("os.name");
    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains("windows")) {
            isWindowsPlatform = true;
        }

        log.info("服务端主机操作系统为: 【{}】", OS_NAME);
    }

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }
    public static boolean isWindowsPlatform() { return isWindowsPlatform; }
}
