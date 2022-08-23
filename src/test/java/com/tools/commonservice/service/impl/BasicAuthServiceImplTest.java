package com.tools.commonservice.service.impl;

import com.tools.commonservice.BasicTest;
import com.tools.commonservice.service.BasicAuthService;
import org.junit.Test;


import javax.annotation.Resource;


public class BasicAuthServiceImplTest  extends BasicTest {
    @Resource
    BasicAuthService basicAuthService;

    @Test
    public void test() {
        basicAuthService.sendBasicRequest();
    }

}