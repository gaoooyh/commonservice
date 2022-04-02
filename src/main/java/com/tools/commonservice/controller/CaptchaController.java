package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.config.RedisService;
import com.tools.commonservice.util.imgUtils.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 验证码接口
 * （需要接入redis
 */
@RestController
@Api("验证码接口")
@RequestMapping("captcha")
public class CaptchaController {

    @Resource
    RedisService redisService;

    @GetMapping("get")
    @ApiOperation("获取图像验证码")
    public HttpResult<Long> getOne(HttpServletRequest request, HttpServletResponse response) throws Exception{

        SpecCaptcha specCaptcha = new SpecCaptcha();

        HttpSession session = request.getSession();
        System.out.println(session.getId() + " : " + specCaptcha.text());
        redisService.set(session.getId(), specCaptcha.text(), 180);
        specCaptcha.out(response.getOutputStream());

        return HttpResult.success();
    }

    @PostMapping("verify")
    @ApiOperation("验证码校验")
    public HttpResult<Boolean> verify(@RequestParam(name = "key", required = true) String code, HttpServletRequest request) {
        String value = redisService.get(request.getSession().getId()).toLowerCase();
        code = code.toLowerCase();

        if (value == null || !value.equals(code)) {
            return HttpResult.failure().setData(false);
        }
        return HttpResult.success().setData(true);
    }

}
