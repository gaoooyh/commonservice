package com.tools.commonservice.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class UserEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String username;
    private String password;
}
