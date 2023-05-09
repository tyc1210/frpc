package com.tyc.frpc.codec.message;


/**
 * 封装请求信息
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-21 17:57:24
 */
public class RpcRequest extends Message{
    private Integer id;
    private String classMethodName;
    private Object[] args;
    private SerializeType serializeType;

    public RpcRequest(Integer id, String methodName, Object[] args,String serializeType) {
        this.id = id;
        this.classMethodName = methodName;
        this.args = args;
        this.serializeType = SerializeType.getByMsg(serializeType);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethodName() {
        return classMethodName;
    }

    public void setMethodName(String methodName) {
        this.classMethodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public SerializeType getSerializeType() {
        return this.serializeType;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.REQUEST;
    }

    @Override
    public Integer getMessageId() {
        return this.id;
    }
}
