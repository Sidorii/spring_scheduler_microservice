package com.sidorii.scheduler.service;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.job.config.JobDescription;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

public interface ScheduleService {

    JobKey addJob(JobConfiguration configuration) throws ConfigurationException;

    void deleteJob(JobKey key);

    JobDetail getJobById(JobKey key);

    JobDescription getJobDescription(JobKey key) throws ConfigurationException;

    default void suspendJob(JobKey key){};

    default void resumeJob(JobKey key){};
}
