package com.tools.commonservice.controller;


import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ConstantsError;
import com.tools.commonservice.util.JsonUtils;
import com.tools.commonservice.util.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring默认的error地址 /error
 * Whitelabel Error Page
 */
@RestController
@Slf4j
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public HttpResult error(HttpServletRequest request) {
        System.out.println("CustomErrorController");
        ApiException ex;
        if((ex = UserContextUtil.getCurrentError()) != null) {
           return HttpResult.setCode(ex.getErrorCode());
        }

        Enumeration<String> attributes = request.getAttributeNames();
        Map<String, Object> map = new HashMap<String, Object>();
        while (attributes.hasMoreElements()) {
            String name = attributes.nextElement();
            if (name.startsWith("java")) {

                Object value = request.getAttribute(name);
                map.put(name, value);
            }
        }

        log.error(JsonUtils.write(map));

        return HttpResult.setCode(ConstantsError.ERROR_SC_NOT_FOUND);
    }

}