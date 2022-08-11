package com.tools.commonservice.service;

import com.tools.commonservice.job.ScheduleJob;

import java.util.List;

public interface JobService {

    List<ScheduleJob> getAllJobs();

    void addJob(String jobName);

    void stopJob(String jobName);

    void addAll();

    void stopAll();
}
