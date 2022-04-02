package com.tools.commonservice.service.impl;

import com.tools.commonservice.worker.SnowflakeWorker;
import com.tools.commonservice.service.SnowflakeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SnowflakeServiceImpl implements SnowflakeService {

    @Resource
    SnowflakeWorker snowflakeWorker;

    @Override
    public Long getOne() {
        return snowflakeWorker.nextId();
    }

    @Override
    public List<Long> getMore(int size) {
        List<Long> result = new ArrayList<>();
        if(size > 0) {
            while(size-- > 0){
                result.add(snowflakeWorker.nextId());
            }
            return result;
        }
        return result;
    }


}
