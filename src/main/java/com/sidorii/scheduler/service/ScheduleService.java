package com.sidorii.scheduler.service;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.job.config.JobDescription;
import com.sidorii.scheduler.model.task.Task;
import org.hibernate.validator.constraints.NotEmpty;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface ScheduleService {

    String addJob(JobConfiguration configuration) throws ConfigurationException;

    void deleteJob(@NotNull @NotEmpty String key);

    JobDescription getJobDescription(@NotNull @NotEmpty String key) throws ConfigurationException;

    default void suspendJob(@NotNull @NotEmpty String key){};

    default void resumeJob(@NotNull @NotEmpty String key){};
}
