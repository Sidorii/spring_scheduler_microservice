package com.sidorii.scheduler.model.service;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public interface ScheduleService {

    void addJob(JobDetail detail, Task task, Trigger trigger);

    void deleteJob(JobKey key);

    JobDetail getJobById(JobKey key);

    void suspendJob(JobKey key);

    void resumeJob(JobKey key);
}
