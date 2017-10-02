package com.sidorii.scheduler.model.service;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

public interface ScheduleService {

    void addJob(JobDetail detail, Trigger trigger);

    void deleteJob(JobKey key);

    JobDetail getJobById(JobKey key);

    void suspendJob(JobKey key);

    void resumeJob(JobKey key);
}
