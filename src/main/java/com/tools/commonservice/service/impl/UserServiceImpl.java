package com.tools.commonservice.service.impl;

import com.tools.commonservice.config.RedisService;
import com.tools.commonservice.data.entity.UserEntity;
import com.tools.commonservice.mapper.UserMapper;
import com.tools.commonservice.service.UserService;
import com.tools.commonservice.util.HashUtil;
import com.tools.commonservice.util.JWTUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    RedisService redisService;

    @Resource
    UserMapper userMapper;

    @Override
    public String loginByAccount(String username, String password) {

        UserEntity user = userMapper.getByName(username);

        if(user != null) {
            String pswInDB = user.getPassword();

            String hashPsw = HashUtil.md5(password);

            if (hashPsw.equals(pswInDB)) {
                String token = JWTUtil.generateToken(username);
                redisService.set(username, token, 60 * 30);
                return token;
            }
        }
        return null;
    }
}
