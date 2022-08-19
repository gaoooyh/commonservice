package com.tools.commonservice.mapper.db2;

import com.tools.commonservice.data.entity.JobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobCopyMapper {

    @Select("select * from t_job where status = 1")
    List<JobEntity> getAllJob();

    @Select("select * from t_job where status = 1 and job_name = #{name} limit 1")
    JobEntity getJobByName(String name);

}
