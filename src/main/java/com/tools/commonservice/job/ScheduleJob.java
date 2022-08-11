package com.tools.commonservice.job;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class ScheduleJob implements Serializable {
    /** 任务id */
    private String jobId;
    /** 任务名称 */
    private String jobName;
    /** 任务分组 */
    private String jobGroup;

    /** 任务运行时间表达式 */
    private String cronExpression;
    /** 任务描述 */
    private String desc;

    private String functionName;

    /** 任务状态 0禁用 1启用*/
    private Integer jobStatus;
}
