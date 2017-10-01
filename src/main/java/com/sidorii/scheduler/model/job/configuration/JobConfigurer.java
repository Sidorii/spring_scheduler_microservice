package com.sidorii.scheduler.model.job.configuration;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface JobConfigurer {

    JobDetail buildJob(JobConfiguration configuration) throws ConfigurationException;

    Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException;
}
