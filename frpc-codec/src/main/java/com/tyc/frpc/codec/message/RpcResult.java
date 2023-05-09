package com.tyc.frpc.codec.message;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-21 18:06:27
 */
public class RpcResult extends Message{
    private Integer code;
    private Integer id;
    private Object resultData;
    private SerializeType serializeType;

    public RpcResult(Integer code, Integer id, Object resultData,String serializeType) {
        this.code = code;
        this.id = id;
        this.resultData = resultData;
        this.serializeType = SerializeType.getByMsg(serializeType);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    @Override
    public SerializeType getSerializeType() {
        return this.serializeType;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.RESPONSE;
    }

    @Override
    public Integer getMessageId() {
        return this.id;
    }
}
