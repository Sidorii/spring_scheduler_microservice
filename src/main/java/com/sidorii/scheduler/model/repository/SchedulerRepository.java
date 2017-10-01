package com.sidorii.scheduler.model.repository;

import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface SchedulerRepository {

    void addJob(JobDetail detail, Trigger trigger);

    void deleteJob(Long id);

    JobDetail getJobById(Long id);
}
