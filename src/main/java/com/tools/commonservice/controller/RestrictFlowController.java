package com.tools.commonservice.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.util.HttpRequestUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("limit")
public class RestrictFlowController {

    @ApiOperation("remoteTest")
    @RequestMapping("remote")
    public HttpResult remote() {
        int n = 14;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        for(int i = 0; i < n; i++) {
            executorService.execute(() -> {
                HttpResult result =  HttpRequestUtil.doGet("http://127.0.0.1:8080/limit/ping",null, HttpResult.class);
                if(result == null) System.out.println("Service blocked");
            });
        }
        return HttpResult.success();
    }

    @ApiOperation("remoteTest")
    @RequestMapping("ping")
    public HttpResult pingPang() {
        return HttpResult.success().setData("pang");
    }


    @ApiOperation("流量限制")
    @RequestMapping("flow")
    public HttpResult flow() throws InterruptedException {

        int cnt = 10;
        while (cnt-- > 0) {
            for (int i = 0; i < 104; i++) {
                try (Entry ignored = SphU.entry("FlowLimit")) {
//                    System.out.println("execute service: " + (int)entry.getCurNode().successQps());

                } catch (BlockException ex) {
                    System.out.println("blocked");
                }
            }
            //因为设置的是qps 所以间隔要睡1s
            Thread.sleep(1000);
        }
        return HttpResult.success();
    }

    @ApiOperation("熔断限制")
    @RequestMapping("degrade")
    public HttpResult degrade() throws InterruptedException {


        int cnt = 10;
        while (cnt-- > 0) {
            for (int i = 0; i <= 5; i++) {
                Entry entry = null;
                try {
                    entry =  SphU.entry("degradeRule");
                    System.out.println("execute service: " + entry.getCurNode().successQps());
                    if(cnt >= 9 || (cnt == 6 && i >=3 )) throw new RuntimeException("test degrade");

                } catch (BlockException ex) {
                    System.out.println("degrade blocked");
                } catch (Exception ex) {
                    System.out.println("trace");
                    Tracer.trace(ex);
                }
                finally {
                    if(entry != null) {
                        entry.exit();
                    }
                }

            }
            //因为设置的是qps 所以间隔要睡1s
            Thread.sleep(1000);
        }
        return HttpResult.success();
    }

}
