package com.tyc.frpc.codec.message;

public class PongMessage extends Message {
    @Override
    public SerializeType getSerializeType() {
        return SerializeType.JSON;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PONG;
    }

    @Override
    public Integer getMessageId() {
        return -1;
    }
}
