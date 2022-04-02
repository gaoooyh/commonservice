package com.tools.commonservice.common;

import lombok.Data;

/**
 * controller返回的数据格式
 * @param <T>
 */
@Data
public class HttpResult<T> {
    private T data;
    private Integer code = 200;
    private String message;

    public static HttpResult failure() {
        return (new HttpResult()).setCode(500).setMessage("server error");
    }

    public static HttpResult success() {
        return (new HttpResult()).setCode(200).setMessage("success");
    }

    public HttpResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public HttpResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }


    public HttpResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

}

