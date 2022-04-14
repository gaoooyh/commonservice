package com.tools.commonservice.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.util.HttpRequestUtil;
import com.tools.commonservice.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("remote")
public class RemoteRequestController {

    @GetMapping("get")
    public HttpResult<Long> get() {
        try (Entry entry = SphU.entry("resourceName")) {


            HttpResult result = JsonUtils.read(HttpRequestUtil.sendRequest("http://localhost:8081/user/tt","GET"), HttpResult.class);
            System.out.println(result);
            return HttpResult.success().setData(result.getData());


        } catch (BlockException ex) {
            log.error(ex.getMessage(),ex);
        }
        return HttpResult.failure();
    }
}
