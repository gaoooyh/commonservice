package com.tools.commonservice.data.entity;

import lombok.Data;

@Data
public class BaseEntity {
    private Long createTime;
    private Long updateTime;
    private String createBy;
    private String updateBy;
}
