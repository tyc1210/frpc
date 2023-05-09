package com.tyc.frpc.codec.message;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-03 15:02:34
 */
public enum SerializeType {
    JSON("json",(byte)0),
    JAVA("java",(byte)1),
    ProtoBuf("protobuf",(byte)2);

    private String msg;
    private Byte code;

    SerializeType(String msg, Byte code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }


    public Byte getCode() {
        return code;
    }

    public static SerializeType getByCode(byte code){
        SerializeType[] values = SerializeType.values();
        for (SerializeType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static SerializeType getByMsg(String msg){
        SerializeType[] values = SerializeType.values();
        for (SerializeType value : values) {
            if (value.msg.equals(msg)) {
                return value;
            }
        }
        return null;
    }
}
