package com.tools.commonservice.job;

import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务管理模块
 * 1. 定时任务初始化
 * 2. 暂停任务/启动任务/停止全部/重启
 */
@Component
public class SchedulerConstruct {

    @Autowired
    private Scheduler scheduler;

    @Resource
    JobConfig jobConfig;

    /**
     * 任务初始化
     */
    @PostConstruct
    public void init() {
        List<ScheduleJob> jobList = jobConfig.getAllJob();
        for (ScheduleJob job : jobList) {
            addScheduleJob(job);
        }
    }

    public List<ScheduleJob> getAllJob(){
        return jobConfig.getAllJob();
    }

    /**
     * 添加ScheduleJob 任务
     */
    private void addScheduleJob(ScheduleJob job) {
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //不存在，创建一个
            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
                        .withIdentity(job.getJobName(), job.getJobGroup()).build();
                jobDetail.getJobDataMap().put("scheduleJob", job);

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                        .getCronExpression());

                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                // Trigger已存在，那么更新相应的定时设置
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();

                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addJob(String jobName) {
        ScheduleJob scheduleJob = jobConfig.addJob(jobName);
        if(scheduleJob != null) {
            addScheduleJob(scheduleJob);
        } else {
            throw new ApiException(ErrorCode.paramError("Job name: " + jobName + " not found."));
        }
    }

    /**
     * 暂停任务
     */
    public void stopJob(String jobName) {
        if(jobConfig.isJobExists(jobName)) {
            ScheduleJob job = jobConfig.getJobByName(jobName);
            if (job != null) {
                try {
                    scheduler.deleteJob(new JobKey(job.getJobName(), job.getJobGroup()));
                    jobConfig.removeJob(jobName);
                } catch (Exception ignored) {

                }
            }
        }
    }


    public void stopAll() {
        try {
            scheduler.clear();
            jobConfig.clear();
        } catch (SchedulerException ignored) {

        }
    }

    public void restartAllJobs() {
        try {
            scheduler.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jobConfig.restartAllJobs();
        init();
    }
}
