package com.tyc.frpc.codec.serialize;

import com.tyc.frpc.codec.message.Message;

import java.io.*;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-09 10:50:52
 */
public class JavaSerialize implements SerializeStrategy{
    @Override
    public Message deserialize(byte[] data, Class clazz) {
        Message message = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInputStream ois = new ObjectInputStream(bis)) {
            message = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public byte[] serialize(Message message) {
        byte[] byteArray = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(message);
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
