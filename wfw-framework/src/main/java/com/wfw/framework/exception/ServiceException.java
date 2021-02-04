package com.wfw.framework.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liaonanzhou
 * @date 2021/2/3 15:28
 * @description
 */
@Data
public class ServiceException extends RuntimeException implements Serializable {

    private static final long serializableUID = 1L;

    private int code = 3000;

    private String msg;

    public ServiceException(String msg){
        super(msg);
        this.msg = msg;
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
