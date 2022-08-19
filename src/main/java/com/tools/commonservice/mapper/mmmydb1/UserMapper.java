package com.tools.commonservice.mapper.mmmydb1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tools.commonservice.data.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    @Insert("insert into t_user(id, username, password) VALUES  (#{id}, #{username}, #{password})")
    void create(String name, int age, String mail);

    @Select("select id, username, password from t_user where username = #{username}")
    UserEntity getByName(String username);

    UserEntity getById2(String username);

    Page<UserEntity> getUsers(Integer id, Page page);


    @Select("select * from t_user")
    List<UserEntity> getAllUsers();
}
