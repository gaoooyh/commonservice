package com.tools.commonservice.job;

import com.tools.commonservice.service.SnowflakeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 具体的定时任务 方法实现类
 * 方法名对应于ScheduleJob 中的functionName
 */
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

    @Resource
    SnowflakeService snowflakeService;

    @Override
    public void generateAndPrintId(String date) {
        System.out.println("Execute task generateAndPrintId, date: [" + date + "]");
        System.out.println("Generate id: " + snowflakeService.getOne() + "  , time: " + LocalDateTime.now());
    }


    @Override
    public void sayHello(String date) {
        System.out.println("Execute task sayHello, date: [" + date + "]");
        System.out.println("Hello world: " + LocalDateTime.now());
    }



}
