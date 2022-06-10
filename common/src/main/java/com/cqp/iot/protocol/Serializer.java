package com.cqp.iot.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName Serializer.java
 * @Description 序列化器 jdk json
 * @TODO protobuf
 * @createTime 2022年05月17日 09:54:00
 */
public enum Serializer implements Serialization{
    /**
     * jdk 自带的序列化方式
     */
    jdk{
        @Override
        public <T> byte[] serialize(T object) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("jdk序列化失败", e);
            }
        }

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("jdk反序列化失败",e);
            }
        }
    },

    /**
     * json 序列化方式
     */
    json{
        ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public <T> byte[] serialize(T object) {

            try {
                String json = objectMapper.writeValueAsString(object);
                return json.getBytes();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("json 序列化失败",e);
            }
        }

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try {
                return objectMapper.readValue(bytes,clazz);
            } catch (IOException e) {
                throw new RuntimeException("json 反序列化失败",e);
            }
        }
    }


}
