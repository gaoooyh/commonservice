package com.tools.commonservice.exception;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Configuration
public class GlobalExceptionResolver implements HandlerExceptionResolver, Ordered {
    public GlobalExceptionResolver() {
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ModelAndView createModelAndView(HttpResult httpResult, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");

        try {
            response.getWriter().write(JsonUtils.write(HttpResult.failure()));
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return new ModelAndView();
    }

    public int getOrder() {
        return -2147483648;
    }

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        HttpResult<Void> result = HttpResult.failure();
        ErrorCode errorCode = null;
        if (ex instanceof ApiException) {
            errorCode = ((ApiException)ex).getErrorCode();
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            errorCode = Constants.ERROR_SC_METHOD_NOT_ALLOWED;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            errorCode = Constants.ERROR_SC_UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            errorCode = Constants.ERROR_SC_NOT_ACCEPTABLE;
        } else if (!(ex instanceof MissingPathVariableException) && !(ex instanceof ConversionNotSupportedException) && !(ex instanceof HttpMessageNotWritableException)) {
            if (ex instanceof NoHandlerFoundException) {
                errorCode = Constants.ERROR_SC_NOT_FOUND;
            } else if (ex instanceof AsyncRequestTimeoutException) {
                errorCode = Constants.ERROR_SC_SERVICE_UNAVAILABLE;
            } else if (!(ex instanceof ServletRequestBindingException) && !(ex instanceof TypeMismatchException) && !(ex instanceof HttpMessageNotReadableException) && !(ex instanceof MissingServletRequestPartException) && !(ex instanceof BindException)) {
                errorCode = ErrorCode.Std_ReqError;
            } else {
                errorCode = Constants.ERROR_SC_BAD_REQUEST;
            }
        } else {
            errorCode = Constants.ERROR_SC_INTERNAL_SERVER_ERROR;
        }

        if (ex instanceof ApiException) {
            log.warn(ex.getMessage());
        } else {
            log.error(ex.getMessage());
        }

        result.setCode(errorCode.code());
        result.setMessage(errorCode.message());
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");

        try {
            httpServletResponse.getWriter().write(JsonUtils.write(result));
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
        }

        return new ModelAndView();
    }

}
