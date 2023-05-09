package com.tyc.frpc.codec.serialize;

import com.tyc.frpc.codec.message.Message;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-09 10:52:14
 */
public class ProtocolSerialize implements SerializeStrategy{
    @Override
    public Message deserialize(byte[] data, Class clazz) {
        return null;
    }

    @Override
    public byte[] serialize(Message message) {
        return new byte[0];
    }
}
