package com.sidorii.scheduler.model.job.configuration;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.newJob;

public class DefaultJobConfigurerImpl implements JobConfigurer {


    @Override
    public JobDetail buildJob(JobConfiguration configuration) throws ConfigurationException {
        JobDetail detail = newJob()
                .usingJobData();

        //implementation required

    }

    @Override
    public Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException {
        return null;
    }
}
