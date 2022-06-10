package com.cqp.iot.protocol;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName Serialization.java
 * @Description 序列化、反序列化
 * @createTime 2022年05月17日 09:53:00
 */
public interface Serialization {
    // 序列化
    <T> byte[] serialize(T object);

    // 反序列化
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
