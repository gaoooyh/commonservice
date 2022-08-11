package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.job.ScheduleJob;
import com.tools.commonservice.service.JobService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {

    @Resource
    JobService jobService;

    @GetMapping("getAllJobs")
    public HttpResult<ScheduleJobInfo> getAllJobs() {
        ScheduleJobInfo info = new ScheduleJobInfo(jobService.getAllJobs());
        return HttpResult.success().setData(info);
    }

    @PostMapping("addJob")
    public HttpResult addJob(String jobName) {
        jobService.addJob(jobName);
        return HttpResult.success();
    }

    @PostMapping("stopJob")
    public HttpResult stopJob(String jobName) {
        jobService.stopJob(jobName);
        return HttpResult.success();
    }

    @PostMapping("stopAll")
    public HttpResult stopAll() {
        jobService.stopAll();
        return HttpResult.success();
    }

    @PostMapping("addAll")
    public HttpResult addAll() {
        jobService.addAll();
        return HttpResult.success();
    }


    @Getter
    @Setter
    public static class ScheduleJobInfo {
        List<ScheduleJob> list;
        public ScheduleJobInfo(List<ScheduleJob> jobList) {
            this.list = jobList;
        }
    }

}
