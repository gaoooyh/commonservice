package com.tools.commonservice.job;

import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDate;

/**
 * 执行定时任务
 */
public class QuartzJobFactory implements Job {

    @Resource
    ScheduleTaskService taskService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务[" + scheduleJob.getJobName() + "], Cron: " + scheduleJob.getCronExpression());
        Class clz = taskService.getClass();

        System.out.println(clz.getSimpleName());

        Method m = clz.getDeclaredMethod(scheduleJob.getFunctionName(), String.class);
        m.invoke(taskService, LocalDate.now().toString());

        //搞一个invoke, 通过反射调用具体要执行的job方法, 可以根据jobName等信息
    }
}
