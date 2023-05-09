package com.tyc.frpc.codec.message;

public class PongMessage extends Message {
    private SerializeType serializeType;

    public PongMessage(String serializeType) {
        this.serializeType = SerializeType.getByMsg(serializeType);
    }

    @Override
    public SerializeType getSerializeType() {
        return this.serializeType;
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
