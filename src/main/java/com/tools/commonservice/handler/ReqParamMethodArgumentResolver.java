package com.tools.commonservice.handler;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import com.tools.commonservice.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

public class ReqParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(ReqParamMethodArgumentResolver.class);

    ObjectMapper mapper = JsonUtils.objectMapper;

    private final Validator validator;


    public ReqParamMethodArgumentResolver(Validator validator) {
        this.validator = validator;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ReqParam.class);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory binderFactory) {
        //获取request
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        assert servletRequest != null;

        Object result = null;

        try {
            Class<?> typeClass = null;
            Type genericParameterType = methodParameter.getGenericParameterType();
            JavaType javaType = null;

//            ParameterizedType 参数化类型, 获取范型的具体类型
            if (genericParameterType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
                //当@ReqParam 标注的对象是一个list时, typeClass 为 java.util.List
                typeClass = Class.forName(parameterizedType.getRawType().getTypeName());
                Type type = parameterizedType.getActualTypeArguments()[0];

                Class pClass;
                if (type instanceof ParameterizedType) {
                    parameterizedType = (ParameterizedType) type;
                    pClass = Class.forName(parameterizedType.getRawType().getTypeName());
                    Type p1Type = parameterizedType.getActualTypeArguments()[0];
                    Class p1Class = Class.forName(p1Type.getTypeName());
                    javaType = JsonUtils.objectMapper.getTypeFactory().constructParametricType(pClass, new Class[]{p1Class});
                    javaType = JsonUtils.objectMapper.getTypeFactory().constructParametricType(typeClass, new JavaType[]{javaType});
                } else {
                    pClass = Class.forName(type.getTypeName());
                    javaType = JsonUtils.objectMapper.getTypeFactory().constructParametricType(typeClass, new Class[]{pClass});
                }
            } else {
                //拿到了Param类
                typeClass = Class.forName(genericParameterType.getTypeName());
            }


            String contentType = servletRequest.getContentType();
            String jsonString = "{}";
            Map<String, String[]> parameterMap = nativeWebRequest.getParameterMap();
            Map<String, Object> params = new HashMap();
            Map<String, String[]> decodedParams = new HashMap();
            Iterator paramIterator = parameterMap.entrySet().iterator();

            Entry e;

            //遍历参数列表
            while (paramIterator.hasNext()) {
                e = (Entry) paramIterator.next();
                String[] oldParams = (String[]) e.getValue();
                String[] newParams = new String[oldParams.length];

                for (int i = 0; i < oldParams.length; ++i) {
                    //为什么判断 "+" 加号
                    //在url中, 如果某个值为 test%2Btest+test
                    // 获取 oldParams 值会发现得到 test+test test， 加号变成了空格

                    //Spring mvc框架在给参数赋值的时候调用了URLDecoder
                    // URLDecoder 把+替换成了空格
                    //解决这个问题，需要前端请求的时候对"+"做处理 编码为%2B

                    //因此 当这里参数中如果有加号, 不能再执行decode, 防止加号丢失
                    //如果参数中没有加号, 执行decode
                    if (oldParams[i].contains("+")) {
                        newParams[i] = oldParams[i];
                    } else {
                        try {
                            newParams[i] = URLDecoder.decode(oldParams[i], "utf-8");
                        } catch (Exception ex) {
                            newParams[i] = oldParams[i];
                        }
                    }
                }

                decodedParams.put((String) e.getKey(), newParams);
            }

            paramIterator = decodedParams.entrySet().iterator();

            while (paramIterator.hasNext()) {
                e = (Entry) paramIterator.next();
                String propertyName = (String) e.getKey();
                if (propertyName.endsWith("[]")) {
                    propertyName = propertyName.substring(0, propertyName.length() - 2);
                }

                if (((String[]) e.getValue()).length == 1) {
                    params.put(propertyName, ((String[]) e.getValue())[0]);

                    try {
                        Field field = typeClass.getDeclaredField(propertyName);
                        if (field != null) {
                            Class<?> fieldType = field.getType();
                            if (fieldType.isArray() || Collection.class.isAssignableFrom(fieldType)) {
                                params.put(propertyName, e.getValue());
                            }
                        }

                    } catch (Exception ex) {
                    }
                } else {
                    params.put(propertyName, e.getValue());
                }
            }

            //把RequestBody中的数据塞到map中
            ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
            if (null != contentType && contentType.toLowerCase().contains("application/json")) {
                try {
                    params.putAll(mapper.readValue(inputMessage.getBody(), HashMap.class));
                } catch (Exception ex) {
                }
            }

            jsonString = mapper.writeValueAsString(params);

            log.info("RequestUri: {}, Param: {}", servletRequest.getRequestURI(), jsonString);
            if (javaType == null) {
                result = this.readFromJsonString(typeClass, jsonString);
            } else {
                result = mapper.readValue(jsonString, javaType);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return result;
    }

    private <T> T readFromJsonString(Class<T> type, String jsonString) throws IOException {
        T t = mapper.readValue(jsonString, type);
        this.validate(t);
        return t;
    }

    private <T> void validate(T obj) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<T>> set = validator.validate(obj, new Class[]{Default.class});
        if (!set.isEmpty()) {
            Iterator msgIterator = set.iterator();

            while (msgIterator.hasNext()) {
                ConstraintViolation<T> cv = (ConstraintViolation) msgIterator.next();
                message.append(cv.getMessage()).append("\n");
            }

            throw new ApiException(ErrorCode.paramError(message.substring(0, message.length()-1)));
        }
    }
}
