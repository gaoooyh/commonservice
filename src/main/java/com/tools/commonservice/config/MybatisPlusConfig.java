package com.tools.commonservice.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tools.commonservice.util.UserContextUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class MybatisPlusConfig {

    //支持分页
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    //对插入和更新的数据自动填充属性
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                Long timestamp = System.currentTimeMillis();
                this.setFieldValByName("createTime", timestamp, metaObject);
                this.setFieldValByName("updateTime", timestamp, metaObject);
                this.setFieldValByName("createBy", UserContextUtil.getCurrentUser(), metaObject);
                this.setFieldValByName("updateBy", UserContextUtil.getCurrentUser(), metaObject);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
                this.setFieldValByName("updateBy", UserContextUtil.getCurrentUser(), metaObject);
            }
        };
    }
}
