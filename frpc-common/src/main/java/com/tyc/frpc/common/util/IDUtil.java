package com.tyc.frpc.common.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-20 16:05:10
 */
public class IDUtil {
    private static AtomicInteger id = new AtomicInteger(0);

    public static String getUUId(){
        return UUID.randomUUID().toString().replace("-","");
    }


    public static Integer getLimitId(){
        Integer result = id.incrementAndGet();
        if(result.equals(Integer.MAX_VALUE)){
            id.set(0);
        }
        return result;
    }
}
