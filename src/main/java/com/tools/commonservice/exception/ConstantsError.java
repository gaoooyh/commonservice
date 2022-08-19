package com.tools.commonservice.exception;


public class ConstantsError {

    public static final ErrorCode ERROR_SC_BAD_REQUEST = ErrorCode.define(400, "错误请求");
    public static final ErrorCode ERROR_NOT_LOGIN = ErrorCode.define(401, "用户未登录");
    public static final ErrorCode ERROR_VERIFY_TOKEN = ErrorCode.define(402, "验证Token失败");
    public static final ErrorCode ERROR_SC_NOT_FOUND = ErrorCode.define(404, "未找到访问的接口");
    public static final ErrorCode ERROR_SC_METHOD_NOT_ALLOWED = ErrorCode.define(405, "非法的访问方法");
    public static final ErrorCode ERROR_SC_NOT_ACCEPTABLE = ErrorCode.define(406, "请求路径缺少参数");
    public static final ErrorCode ERROR_SC_UNSUPPORTED_MEDIA_TYPE = ErrorCode.define(415, "非法的MediaType");

    public static final ErrorCode ERROR_SC_INTERNAL_SERVER_ERROR = ErrorCode.define(500, "内部错误");
    public static final ErrorCode ERROR_SC_SERVICE_UNAVAILABLE = ErrorCode.define(503, "服务不可用");
    public static final ErrorCode ERROR_DUPLICATED_KEY = ErrorCode.define(10001, "重复的活动名称");
}
