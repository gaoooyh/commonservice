package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.annotation.ReqParam;
import com.tools.commonservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆验证接口
 */
@RestController
@Api("登陆验证接口")
@RequestMapping("login")
public class LoginController {
    @Resource
    UserService userService;

    /**
     * username: username
     * password: passwordInDb
     * @param param
     * @param response
     * @return
     */
    @PostMapping("/byAccount")
    @ApiOperation("通过账号登陆")
    public HttpResult<String> loginByAccount(@ReqParam AccountParam param, HttpServletResponse response) {
        String token = userService.loginByAccount(param.getUsername(), param.getPassword());
        if(token != null) {
            response.addHeader("MyToken", token);
            return HttpResult.success();
        } else {
            return HttpResult.failure().setMessage("用户名或密码错误");
        }
    }



    @Data
    static class AccountParam {
        private String username;
        private String password;
    }
}
