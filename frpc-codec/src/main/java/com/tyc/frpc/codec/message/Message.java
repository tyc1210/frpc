package com.tyc.frpc.codec.message;

import java.io.Serializable;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-03 14:45:52
 */
public abstract class Message implements Serializable {
    // 获取序列化方式
    public abstract SerializeType getSerializeType();
    // 获取消息方类型
    public abstract MessageType getMessageType();

    public abstract Integer getMessageId();

}
