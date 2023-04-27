package com.tyc.frpc.codec.message;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-03 15:02:34
 */
public enum SerializeType {
    JSON("JSON",(byte)0);

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
}
