package com.tyc.client.test;

import com.tyc.frpc.client.annotation.RpcReference;
import com.tyc.frpc.client.util.ScanUtil;

import java.util.Set;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-24 17:54:25
 */
public class Application {
    public static void main(String[] args) {
        Set<Class<?>> classes = ScanUtil.scanAnnotationField("com.tyc.test", RpcReference.class);
        System.out.println(classes);
    }
}
