package com.tyc.frpc.codec.serialize;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.codec.message.Message;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-08 09:43:36
 */
public class JSONSerialize implements SerializeStrategy{

    @Override
    public Message deserialize(byte[] data, Class clazz) {
        return JSONObject.parseObject(data,clazz);
    }

    @Override
    public byte[] serialize(Message message) {
        return JSONObject.toJSONString(message).getBytes();
    }
}
