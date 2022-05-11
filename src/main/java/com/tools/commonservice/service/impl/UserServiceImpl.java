package com.tools.commonservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tools.commonservice.common.HttpPage;
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

    /**
     * 分页获取 demo
     * @param pageSize 一页大小
     * @param currentPage 当前第currentPage页
     * @return pageData
     */
    @Override
    public HttpPage<UserEntity> getPageData(int pageSize, int currentPage) {

        Page page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);

        //带条件查询
        Page<UserEntity> pageData = this.userMapper.getUsers(5, page);
        HttpPage<UserEntity> result = new HttpPage<>();

        result.setData(pageData.getRecords());
        result.setTotal((int)pageData.getTotal());
        result.setPage((int)pageData.getCurrent());
        result.setPageSize((int)pageData.getSize());

        return result;
    }
}
