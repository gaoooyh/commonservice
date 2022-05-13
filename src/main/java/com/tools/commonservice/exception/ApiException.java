package com.tools.commonservice.exception;

import lombok.Getter;

public class ApiException extends RuntimeException {

    @Getter
    private ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.message(), null);
        this.errorCode = errorCode;
    }

    public ApiException(ErrorCode errorCode, Throwable t) {
        super(errorCode.message(), t);
        this.errorCode = errorCode;
    }
}
