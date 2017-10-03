package com.sidorii.scheduler.model.repository;

import com.sidorii.scheduler.model.exception.JobNotFoundException;
import com.sidorii.scheduler.model.service.ScheduleService;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("scheduleRepository")
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private Scheduler scheduler;

    @Autowired
    public ScheduleRepositoryImpl(Scheduler scheduler) throws SchedulerException {
        this.scheduler = scheduler;
    }


    @Override
    public void addJob(JobDetail detail, Trigger trigger) throws SchedulerException {
        JobKey key = detail.getKey();

        if (scheduler.checkExists(key)) {
            throw new RuntimeException("Duplicating job while adding new instance to ScheduleService");
        }

        scheduler.scheduleJob(detail, trigger);
    }

    @Override
    public void deleteJob(JobKey key) throws SchedulerException {
        throwExcIfAbsent(key, "deleteJob()");

        scheduler.deleteJob(key);
    }

    @Override
    public JobDetail getJobById(JobKey key) throws SchedulerException {
        throwExcIfAbsent(key, "getJobById()");

        return scheduler.getJobDetail(key);
    }

    @Override
    public void suspendJob(JobKey key) throws SchedulerException {
        throwExcIfAbsent(key, "suspendJob()");

        scheduler.pauseJob(key);
    }

    @Override
    public void resumeJob(JobKey key) throws SchedulerException {
        throwExcIfAbsent(key, "resumeJob()");

        scheduler.resumeJob(key);
    }

    private void throwExcIfAbsent(JobKey key, String method) throws SchedulerException {
        if (!scheduler.checkExists(key)) {
            throw new JobNotFoundException("Job with key = " + key +
                    " not found while proceeding " + method + " method");
        }
    }
}
