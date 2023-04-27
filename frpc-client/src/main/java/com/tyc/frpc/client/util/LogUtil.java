package com.tyc.frpc.client.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印ByteBuf工具类
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-03 17:20:24
 */
public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void log(ByteBuf buf) {
        StringBuilder sb = new StringBuilder();
        //读索引
        sb.append(" read index:").append(buf.readerIndex());
        //写索引
        sb.append(" write index:").append(buf.writerIndex());
        //容量
        sb.append(" capacity :").append(buf.capacity());
        ByteBufUtil.appendPrettyHexDump(sb, buf);
        logger.debug(sb.toString());
//        System.out.println(sb.toString());
    }
}
