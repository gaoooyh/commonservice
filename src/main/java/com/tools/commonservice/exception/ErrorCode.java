package com.tools.commonservice.exception;

public class ErrorCode {

        private final int code;
        private final String message;

        public static final ErrorCode Std_ReqError = define(500, "请求错误");

        private ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static final ErrorCode paramError(String message) {
            return new ErrorCode(500, message);
        }

        public static final ErrorCode define(int code, String message) {
            return new ErrorCode(code, message);
        }

        public int code() {
            return code;
        }

        public String message() {
            return message;
        }
    }

