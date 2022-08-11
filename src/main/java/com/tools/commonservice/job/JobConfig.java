package com.tools.commonservice.job;

import com.tools.commonservice.data.entity.JobEntity;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import com.tools.commonservice.mapper.JobMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Component
public class JobConfig {
    /** 计划任务map */
    private static final Map<String, ScheduleJob> jobMap = new HashMap<String, ScheduleJob>();


    @Resource
    JobMapper jobMapper;

    /**
     * 项目初始化, 获取所有启用job并添加到map中
     */
    @PostConstruct
    public void init() {
        List<JobEntity> jobEntityList = jobMapper.getAllJob();
        for(JobEntity entity : jobEntityList) {
            ScheduleJob job = toScheduleJob(entity);
            if(entity.getStatus() == 1) {
                addJob(job);
            }
        }
    }

    /**
     * 添加任务
     */
    private void addJob(ScheduleJob scheduleJob) {
        log.info("add job id: {}", scheduleJob.getJobId());
        jobMap.put(scheduleJob.getJobName(), scheduleJob);
    }


    private ScheduleJob toScheduleJob(JobEntity entity) {
        ScheduleJob job = new ScheduleJob();
        job.setJobId("Job_" + entity.getId());
        job.setJobName(entity.getJobName());

        //如果有需要Job分组, 在jobEntity中加一下这个字段
        job.setJobGroup(entity.getJobName());
        job.setFunctionName(entity.getFunctionName());

        job.setJobStatus(entity.getStatus());
        job.setCronExpression(entity.getCron());
        job.setDesc(entity.getDesc());
        return job;
    }

    /**
     * 添加job, 某个job remove后重新加入到定时任务中
     */
    public ScheduleJob addJob(String jobName) {
        if(isJobExists(jobName)) {
            throw new ApiException(ErrorCode.paramError("Job: "+ jobName + " already exists." ));
        }

        ScheduleJob scheduleJob = getJobByName(jobName);
        if(scheduleJob != null) {
            addJob(scheduleJob);
        }
        return scheduleJob;
    }


    /**
     * 获取当前Job列表
     */
    public List<ScheduleJob> getAllJob(){
        Iterator<Map.Entry<String, ScheduleJob>> entries = jobMap.entrySet().iterator();
        List<ScheduleJob> list = new ArrayList<>();
        while (entries.hasNext()) {
            Map.Entry<String, ScheduleJob> entry = entries.next();
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * 移除一个job
     */
    public void removeJob(String jobName) {
        jobMap.remove(jobName);
    }

    /**
     * 当前没有配groupName
     */
    public void stopGroupJob(String groupName){
        Collection<ScheduleJob> jobList = jobMap.values();
        for(ScheduleJob job : jobList) {
            if(job.getJobGroup().equals(groupName)) {
                jobMap.remove(job.getJobName(), job);
            }
        }
    }

    public ScheduleJob getJobByName(String jobName) {
        if(isJobExists(jobName)) {
            return jobMap.get(jobName);
        }

        JobEntity entity = jobMapper.getJobByName(jobName);
        if(entity == null) {
            return null;
        }
        return toScheduleJob(entity);
    }

    public boolean isJobExists(String jobName) {
        return jobMap.containsKey(jobName);
    }

    public void restartAllJobs() {
        jobMap.clear();
        init();
    }

    public void clear() {
        jobMap.clear();
    }


}
