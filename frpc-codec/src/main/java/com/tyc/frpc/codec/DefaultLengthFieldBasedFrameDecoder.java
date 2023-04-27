package com.tyc.frpc.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 利用 netty 的LengthFieldBasedFrameDecoder 处理粘包、半包
 * @author tyc
 */
public class DefaultLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {
    public DefaultLengthFieldBasedFrameDecoder(){
        this(1024,12,4,0,0);
    }

    public DefaultLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
