package com.tools.commonservice.service.impl;

import com.tools.commonservice.job.ScheduleJob;
import com.tools.commonservice.job.SchedulerConstruct;
import com.tools.commonservice.service.JobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {


    @Resource
    SchedulerConstruct schedulerConstruct;

    @Override
    public List<ScheduleJob> getAllJobs() {
        return schedulerConstruct.getAllJob();
    }

    @Override
    public void addJob(String jobName) {
        schedulerConstruct.addJob(jobName);
    }

    @Override
    public void stopJob(String jobName) {
        schedulerConstruct.stopJob(jobName);
    }

    @Override
    public void addAll() {
        schedulerConstruct.restartAllJobs();
    }

    @Override
    public void stopAll() {
        schedulerConstruct.stopAll();
    }


}
