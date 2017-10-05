package com.sidorii.scheduler.repository;

import org.quartz.*;
import java.util.List;

public interface ScheduleRepository {


    void addJob(JobDetail detail, Trigger trigger) throws SchedulerException;

    void addJob(JobDetail detail, Trigger trigger, SchedulerListener schedulerListener) throws SchedulerException;

    void deleteJob(JobKey key) throws SchedulerException;

    JobDetail getJobById(JobKey key) throws SchedulerException;

    List<? extends Trigger> getTriggersForJob(JobKey key) throws SchedulerException;

    default void suspendJob(JobKey key) throws SchedulerException {}

    default void resumeJob(JobKey key) throws SchedulerException{};

}
