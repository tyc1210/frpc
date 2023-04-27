package com.tyc.frpc.codec.message;

public class PingMessage extends Message {
    @Override
    public SerializeType getSerializeType() {
        return SerializeType.JSON;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PING;
    }

    @Override
    public Integer getMessageId() {
        return -1;
    }
}
