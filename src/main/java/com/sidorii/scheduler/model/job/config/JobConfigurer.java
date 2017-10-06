package com.sidorii.scheduler.model.job.config;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface JobConfigurer {

    String TIME_ZONE = "time_zone";
    String TYPE = "type";
    String CALLBACK_URL = "callback_url";
    String CODE = "code";
    String BODY = "body";


    JobDetail buildJob(JobConfiguration configuration) throws ConfigurationException;

    Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException;

    JobDescription buildJobDescription(JobDetail detail, Trigger trigger, Task task);
}
