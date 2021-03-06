package com.tools.commonservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;


/**
 * 是否需要验证登陆, 详见AccessFilter
 * openList 不需要token即可直接请求
 * 其他接口均需要token才能通过
 *
 * whiteList和blackList暂未使用
 */
@Data
@ConfigurationProperties(prefix = "com.access")
@Component
public class AccessConfig {

    private List<String> whiteList;

    private List<String> blackList;

    private List<String> openList;
}
