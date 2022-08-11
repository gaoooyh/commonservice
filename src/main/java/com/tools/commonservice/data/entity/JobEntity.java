package com.tools.commonservice.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_job")
public class JobEntity {
    private Integer id;

    private String jobName;

    //暂未用到, 可以作为jobGroup使用
    private Integer type;

    private String cron;

    private String desc;

    private String functionName;

    private Integer status;
}
