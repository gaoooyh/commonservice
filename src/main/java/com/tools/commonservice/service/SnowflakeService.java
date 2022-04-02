package com.tools.commonservice.service;

import java.util.List;

public interface SnowflakeService {
    Long getOne();

    List<Long> getMore(int size);
}
