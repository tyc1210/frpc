package com.tyc.frpc.codec.util;

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

    public static String log(ByteBuf buf) {
        StringBuilder sb = new StringBuilder();
        //读索引
        sb.append(" read index:").append(buf.readerIndex());
        //写索引
        sb.append(" write index:").append(buf.writerIndex());
        //容量
        sb.append(" capacity :").append(buf.capacity());
        ByteBufUtil.appendPrettyHexDump(sb, buf);
        return "\n"+sb.toString();
//        logger.info("\n"+sb.toString());
//        System.out.println(sb.toString());
    }
}
