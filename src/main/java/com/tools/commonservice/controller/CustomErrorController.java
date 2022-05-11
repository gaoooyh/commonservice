package com.tools.commonservice.controller;


import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.util.JsonUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public HttpResult<Map> error(HttpServletRequest request) {
        System.out.println("CustomErrorController");


        Enumeration<String> attributes = request.getAttributeNames();
        Map<String, Object> map = new HashMap<String, Object>();
        while (attributes.hasMoreElements()) {
            String name = attributes.nextElement();
            if (name.startsWith("java")) {

                Object value = request.getAttribute(name);
                map.put(name, value);
            }
        }

        System.out.println(map);

        return HttpResult.failure().setMessage("请求路径有误");
    }

}