package com.tyc.frpc.codec.message;

public class PingMessage extends Message {
    private SerializeType serializeType;

    public PingMessage(String serializeType) {
        this.serializeType = SerializeType.getByMsg(serializeType);
    }

    @Override
    public SerializeType getSerializeType() {
        return this.serializeType;
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
