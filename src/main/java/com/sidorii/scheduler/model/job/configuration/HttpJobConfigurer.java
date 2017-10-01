package com.sidorii.scheduler.model.job.configuration;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.HttpJob;
import com.sidorii.scheduler.model.task.HttpTask;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class HttpJobConfigurer implements JobConfigurer {

    @Override
    public JobDetail buildJob(JobConfiguration configuration) throws ConfigurationException {

        if (!(configuration instanceof HttpJobConfiguration)) {
            throw new ConfigurationException("Cannot configure HttpJob from not HttpConfiguration instance");
        }

        HttpTask task = ((HttpJobConfiguration) configuration).getTask();

        JobDetail job = newJob(HttpJob.class)
                .usingJobData("task", task.toString())
                .build();

        return job;
    }

    @Override
    public Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException {

        return newTrigger()
                .withSchedule(cronSchedule(configuration.getScheduledAt())
                        .inTimeZone(configuration.getTimeZone()))
                .startAt(configuration.getStartTime())
                .endAt(configuration.getEndTime())
                .build();
    }
}
