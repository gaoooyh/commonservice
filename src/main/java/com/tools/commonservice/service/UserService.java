package com.tools.commonservice.service;

import com.tools.commonservice.common.HttpPage;
import com.tools.commonservice.data.entity.UserEntity;

public interface UserService {
    String loginByAccount(String username, String password);

    HttpPage<UserEntity> getPageData(int pageSize, int page);
}
