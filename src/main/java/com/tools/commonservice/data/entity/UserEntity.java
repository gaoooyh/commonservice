package com.tools.commonservice.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("t_user")
@EqualsAndHashCode(callSuper = false)
public class UserEntity  extends BaseEntity{
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
}
