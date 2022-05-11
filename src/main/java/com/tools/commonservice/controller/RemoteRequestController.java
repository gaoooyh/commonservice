package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.annotation.ReqParam;
import com.tools.commonservice.util.HttpRequestUtil;
import com.tools.commonservice.util.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("remote")
public class RemoteRequestController {

    @GetMapping("testGet")
    public HttpResult testGet() {
        UserParam paramObj = new UserParam();
        paramObj.setPassword("post+Password");
        paramObj.setUsername(Arrays.asList(new String[]{"gaoooyh", "Inghayo"}));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("paramMap","paramMap");
        paramMap.put("paramObj", paramObj);

        Map<String, Object> map = new HashMap<>();
        map.put("password", "request+Get");
        map.put("username", Arrays.asList(new String[]{"gaoooyh", "Inghayo"}));
        map.put("otherParam", paramObj);
        map.put("paramMap", paramMap);
        HttpResult result = HttpRequestUtil.doGet("http://localhost:8080/remote/remoteGet", map, HttpResult.class);

        System.out.println("testGet response : " + result);
        Map response = (Map) result.getData();
        Iterator iterator = response.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            System.out.println(key + " :" + response.get(key));
        }

        UserParam formatUser = JsonUtils.read(JsonUtils.write(result.getData()), UserParam.class);
        System.out.println(formatUser);

        return HttpResult.success().setData(result.getData());
    }


    @PostMapping("testPost")
    public HttpResult testPost() {
        UserParam param = new UserParam();
        param.setPassword("post+Password");
        param.setUsername(Arrays.asList(new String[]{"gaoooyh", "Inghayo"}));

        HttpResult result = HttpRequestUtil.doPost("http://localhost:8080/remote/remotePost", JsonUtils.write(param), HttpResult.class);

        System.out.println("testPost response : " + result);

        Map response = (Map) result.getData();
        Iterator iterator = response.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            System.out.println(key + " :" + response.get(key));
        }

        return HttpResult.success().setData(result.getData());
    }

    @GetMapping("remoteGet")
    public HttpResult remoteGet(
            //@ReqParam 加这个注解使用ReqParamMethodArgumentResolver实现的
            // 不加或者写@RequestParam 会调用SpringMVC提供的RequestParamMethodArgumentResolver
            UserParam userParam) {

        System.out.println("#RemoteGet# ---userParam: " + userParam);
        UserParam otherParam = JsonUtils.read(userParam.getOtherParam(),UserParam.class);
        System.out.println("#RemoteGet# ---" + otherParam);

        Map map = JsonUtils.read(userParam.getParamMap(), Map.class);
        System.out.println("#RemoteGet# ---paramMap: " + map.get("paramMap"));
        String paramObjStr = JsonUtils.write(map.get("paramObj"));

        System.out.println("#RemoteGet# ---paramObj: " + JsonUtils.read(paramObjStr, UserParam.class));


        UserParam result = new UserParam();
        result.setPassword("response+password");
        result.setUsername(Arrays.asList(new String[]{"this","is", "response"}));

        return HttpResult.success().setData(result);
    }

    @PostMapping("remotePost")
    public HttpResult remotePost(
            //@ReqParam 加这个注解使用ReqParamMethodArgumentResolver实现的
            // 写@RequestBody 会读application/json的数据 执行RequestResponseBodyMethodProcessor
            // 否则是默认的@RequestParam RequestParamMethodArgumentResolver
            @ReqParam UserParam userParam) {

        System.out.println("#RemotePost# ---userParam: " + userParam);

        UserParam result = new UserParam();
        result.setPassword("response+password");
        result.setUsername(Arrays.asList(new String[]{"this","is", "response"}));

        return HttpResult.success().setData(result);
    }



    @Data
    static class UserParam {
        private List<String> username;
        private String password;
        private String otherParam;
        private String paramMap;
    }
}
