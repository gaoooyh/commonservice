package com.tools.commonservice.handler;

import java.lang.annotation.*;

/**
 * 获取requestHeader 和requestBody中的参数
 * 如果key xxxKey 同时在header 和body 中
 * body的值会覆盖header的值
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqParam {
}

