package com.tools.commonservice.config;

import com.tools.commonservice.filter.AccessFilter;
import com.tools.commonservice.filter.ParamLogFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import javax.servlet.Filter;


/**
 * 配置过滤器
 * 在请求进到controller之前和结束controller之后 对数据进行校验
 * accessFilter 检查token并过滤请求
 *
 *
 * 已删除的 exceptionFilter 捕获出现的异常
 * springboot自带的异常处理会先于exceptionFilter对异常进行处理, 并对异常进行封装
 * 因此异常处理不采用filter方式, 改按spring提供的方式进行处理
 *
 * paramLogFilter 打印请求uri和参数
 */
@Configuration
@AutoConfigureAfter({RedisService.class, AccessConfig.class})
public class FilterConfig {
    @Resource
    private AccessConfig accessConfig;

    @Resource
    private RedisService redisService;


    public Filter accessFilter() {
        return new AccessFilter(accessConfig, redisService);
    }


    public Filter paramLogFilter() {
        return new ParamLogFilter();
    }

    @Bean(name = "registerParamLogFilter")
    public FilterRegistrationBean registerParamLogFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(paramLogFilter());
        filterRegistrationBean.setOrder(-1);
        return filterRegistrationBean;
    }

    @Bean(name = "registerAccessFilter")
    public FilterRegistrationBean registerAccessFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(accessFilter());
        filterRegistrationBean.setOrder(-2);
        return filterRegistrationBean;
    }

}
