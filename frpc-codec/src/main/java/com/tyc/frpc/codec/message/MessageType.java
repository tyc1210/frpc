package com.tyc.frpc.codec.message;

public enum  MessageType {
    REQUEST("request",(byte)0),
    RESPONSE("response",(byte)1),
    PING("ping",(byte)2),
    PONG("pong",(byte)3);
    private String msg;
    private Byte code;

    MessageType(String msg, Byte code) {
        this.msg = msg;
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    MessageType(String msg) {
        this.msg = msg;
    }

    public static Class getClass(MessageType type){
        switch (type){
            case REQUEST:
                return RpcRequest.class;
            case RESPONSE:
                return RpcResult.class;
            case PING:
                return PingMessage.class;
            case PONG:
                return PongMessage.class;
            default:
                return null;
        }
    }

    public static MessageType getByCode(Byte code){
        MessageType[] values = MessageType.values();
        for (MessageType value : values) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }
}
