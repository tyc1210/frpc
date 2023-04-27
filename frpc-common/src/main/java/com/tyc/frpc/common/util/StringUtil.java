package com.tyc.frpc.common.util;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-22 16:47:11
 */
public class StringUtil {
    /**
     * 首字母转小写
     */
    public static String captureName(String str){
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
