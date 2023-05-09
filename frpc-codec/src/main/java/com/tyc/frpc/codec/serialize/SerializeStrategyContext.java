package com.tyc.frpc.codec.serialize;



import com.tyc.frpc.codec.message.Message;
import com.tyc.frpc.codec.message.MessageType;
import com.tyc.frpc.codec.message.SerializeType;

import java.util.Objects;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-08 09:29:33
 */
public class SerializeStrategyContext {
    private SerializeStrategy serializeStrategy;

    public SerializeStrategyContext(Byte serializeType) {
        SerializeType type = SerializeType.getByCode(serializeType);
        switch (type){
            case JSON:
                serializeStrategy = new JSONSerialize();
                break;
            case JAVA:
                serializeStrategy = new JavaSerialize();
                break;
            case ProtoBuf:
                serializeStrategy = new ProtocolSerialize();
            default:
                break;
        }
    }



    public Message deSerialize(byte[] data, MessageType messageType){
        Class aClass = MessageType.getClassByType(messageType);
        if(Objects.isNull(aClass) || Objects.isNull(serializeStrategy)){
            return null;
        }
        return serializeStrategy.deserialize(data,aClass);
    }

    public byte[] serialize(Message message){
        if(Objects.isNull(serializeStrategy)){
            return null;
        }
        return serializeStrategy.serialize(message);
    }
}
