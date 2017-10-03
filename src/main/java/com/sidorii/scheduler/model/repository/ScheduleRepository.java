package com.sidorii.scheduler.model.repository;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public interface ScheduleRepository {


    void addJob(JobDetail detail, Trigger trigger) throws SchedulerException;

    void deleteJob(JobKey key) throws SchedulerException;

    JobDetail getJobById(JobKey key) throws SchedulerException;

    void suspendJob(JobKey key) throws SchedulerException;

    void resumeJob(JobKey key) throws SchedulerException;
}
