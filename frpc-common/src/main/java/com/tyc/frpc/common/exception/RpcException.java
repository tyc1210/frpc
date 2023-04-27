package com.tyc.frpc.common.exception;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-09 10:27:07
 */
public class RpcException extends RuntimeException{
    public RpcException(String message) {
        super(message);
    }
}
