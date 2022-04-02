package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.service.SnowflakeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 雪花算法接口
 */
@RestController
@RequestMapping("snowflake")
public class SnowflakeController {

    //最大支持一次性获取的id数量 1024
    private final Integer maxSizeBits = 10;
    private final Long maxSize= ~(-1L << maxSizeBits);

    @Resource
    private SnowflakeService snowFlakeService;


    /**
     * 获取单个id
     * @return id
     * 是否需要转string?
     */
    @GetMapping("getOne")
    @ApiOperation("获取一条id")
    public HttpResult<Long> getOne() {
        return HttpResult.success().setData(snowFlakeService.getOne());
    }

    /**
     * 批量获取id
     * @param param size <= 1024
     * @return array
     */
    @GetMapping("getMore")
    @ApiOperation("获取多条id")
    public HttpResult<List<Long>> getMore(@RequestParam(name = "size", required = false) Integer param) {
        int size = 0;
        if (param != null && param > 0) {
            size = param <= maxSize ? param : maxSize.intValue();
        }
        return HttpResult.success().setData(snowFlakeService.getMore(size));
    }


}
