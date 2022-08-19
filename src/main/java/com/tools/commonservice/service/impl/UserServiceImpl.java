package com.tools.commonservice.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tools.commonservice.common.HttpPage;
import com.tools.commonservice.config.RedisService;
import com.tools.commonservice.data.entity.UserEntity;
import com.tools.commonservice.mapper.mmmydb1.UserMapper;
import com.tools.commonservice.service.UserService;
import com.tools.commonservice.util.HashUtil;
import com.tools.commonservice.util.JWTUtil;
import com.tools.commonservice.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    RedisService redisService;

    @Resource
    UserMapper userMapper;

    @Override
    public String loginByAccount(String username, String password, String agent) {

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
        result.setCurrent((int)pageData.getCurrent());
        result.setPageSize((int)pageData.getSize());

        return result;
    }



    @Override
    public HttpPage<UserEntity> getCustomPageData(int pageSize, int currentPage) {

        //带条件查询
        List<UserEntity> userList = this.userMapper.getAllUsers();

        //复杂排序... demo
        if(CollectionUtils.isNotEmpty(userList)) {
            userList.sort((o1, o2) -> {
                int compareVal1 = o1.getOrderColumn1().compareTo(o2.getOrderColumn1());
                if (compareVal1 == 0) {
                    int compareVal2 = o2.getOrderColumn2().compareTo(o1.getOrderColumn2());
                    if (compareVal2 == 0) {
                        return o1.getOrderColumn3().compareTo(o2.getOrderColumn3());
                    } else {
                        return compareVal2;
                    }
                } else {
                    return compareVal1;
                }
            });
        }

        return PageUtil.page(currentPage, pageSize, userList);
    }

    @Override
    public void createUser(String username, String password) {

        UserEntity user = new UserEntity();
        user.setUsername(username);
        String hashPsw = HashUtil.md5(password);
        user.setPassword(hashPsw);
        userMapper.insert(user);

    }


}
