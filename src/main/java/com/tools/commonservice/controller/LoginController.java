package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpPage;
import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.annotation.ReqParam;
import com.tools.commonservice.data.constant.ConstantsKey;
import com.tools.commonservice.data.entity.UserEntity;
import com.tools.commonservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    public HttpResult<String> loginByAccount(@ReqParam AccountParam param, HttpServletRequest request, HttpServletResponse response) {
        String agent = request.getHeader(ConstantsKey.HTTP_REQUEST_HEADER_USER_AGENT);

        String token = userService.loginByAccount(param.getUsername(), param.getPassword(), agent);
        if(token != null) {
            response.addHeader(ConstantsKey.TOKEN_HEADER, token);
            response.addCookie(new Cookie(ConstantsKey.TOKEN_HEADER, token));
            return HttpResult.success();
        } else {
            return HttpResult.failure().setMessage("用户名或密码错误");
        }
    }

    @PostMapping("/create")
    @ApiOperation("创建新用户")
    public HttpResult<String> create(@ReqParam AccountParam param, HttpServletResponse response) {
        userService.createUser(param.getUsername(), param.getPassword());

        return HttpResult.success();

    }

    @RequestMapping("/agent")
    public HttpResult<Void> agent(HttpServletRequest request) {
        System.out.println("---------------agent: " + request.getHeader("User-Agent"));

        return HttpResult.success();

    }


    @GetMapping("/getAll")
    @ApiOperation("获取账号")
    public HttpResult<HttpPage<UserEntity>> loginByAccount() {
        return HttpResult.success().setData(userService.getPageData(10,1));
    }


    @GetMapping("/getAllUserByPage")
    @ApiOperation("分页获取账号")
    public HttpResult<HttpPage<UserEntity>> getAllUserByPage() {
        return HttpResult.success().setData(userService.getCustomPageData(10,1));
    }

    @Data
    static class AccountParam {
        private String username;
        private String password;
    }
}
