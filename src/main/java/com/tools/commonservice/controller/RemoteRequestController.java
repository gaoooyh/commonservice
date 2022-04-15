package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.handler.ReqParam;
import com.tools.commonservice.util.HttpRequestUtil;
import com.tools.commonservice.util.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("remote")
public class RemoteRequestController {

    @GetMapping("testGet")
    public HttpResult testGet() {

        Map<String, Object> map = new HashMap<>();
        map.put("password", "requestGet");
        map.put("username", "usernameGyh");
        map.put("otherParam", "123");
        HttpResult result = HttpRequestUtil.doGet("http://localhost:8080/remote/test", map, HttpResult.class);

        System.out.println("testGet response : " + result);
        Map response = (Map) result.getData();
        Iterator iterator = response.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            System.out.println(key + " :" + response.get(key));
        }


        return HttpResult.success();
    }


    @PostMapping("testPost")
    public HttpResult testPost() {
        UserParam param = new UserParam();
        param.setPassword("postPassword");
        param.setUsername(Arrays.asList(new String[]{"gaoooyh", "Inghayo"}));

        HttpResult result = HttpRequestUtil.doPost("http://localhost:8080/remote/test", JsonUtils.write(param), HttpResult.class);

        System.out.println("testPost response : " + result);

        Map response = (Map) result.getData();
        Iterator iterator = response.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            System.out.println(key + " :" + response.get(key));
        }

        return HttpResult.success().setData(result.getData());
    }

    @RequestMapping("test")
    public HttpResult test(UserParam userParam) {
        System.out.println(userParam);

        UserParam result = new UserParam();
        result.setPassword("response password");
        result.setUsername(Arrays.asList(new String[]{"this","is", "response"}));

        return HttpResult.success().setData(result);
    }



    @Data
    static class UserParam {
        private List<String> username;
        private String password;
    }
}
