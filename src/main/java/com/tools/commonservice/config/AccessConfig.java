package com.tools.commonservice.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;


/**
 * 是否需要验证登陆, 详见AccessFilter
 * openList 不需要任何验证方式即可直接请求
 * authList 只通过简单的basic认证即可请求 authInfo为对应的用户名密码
 * whiteList 需要携带token才能通过验证
 * blackList 暂未开发
 */
@Data
@ConfigurationProperties(prefix = "com.access")
@Component
public class AccessConfig {


    private List<String> whiteList;

    private List<String> blackList;

    private List<String> openList;

    private List<String> authList;

    private AccessInfo authInfo;

    @Getter
    @Setter
    public static class AccessInfo {
        private String username;
        private String password;
    }
}
