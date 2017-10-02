package com.sidorii.scheduler.model.service;

import com.sidorii.scheduler.model.exception.JobNotFoundException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleServiceImpl implements ScheduleService {

//    TODO: Add ScheduleRepository and fix boilerplate exceptions

    private Scheduler scheduler;

    @Autowired
    public ScheduleServiceImpl(Scheduler scheduler) throws SchedulerException {
        this.scheduler = scheduler;
    }


    @Override
    public void addJob(JobDetail detail, Trigger trigger) {
        try {
            if (scheduler.checkExists(detail.getKey())){
                throw new RuntimeException("Duplicating job while adding new instance to ScheduleService");
            }
            scheduler.scheduleJob(detail, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Attempt to schedule job [ " + detail.getKey() + " ] with trigger [ "
                    + trigger.getKey() + " ] is failed");
        }
    }

    @Override
    public void deleteJob(JobKey key) {
        try {
            throwIfAbsent(key,"deleteJob()");
            scheduler.deleteJob(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed deleting job [" + key + " ]. Nested exception: " + e);
        }
    }

    @Override
    public JobDetail getJobById(JobKey key) {
        try {
            throwIfAbsent(key, "getJobById()");
            return scheduler.getJobDetail(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed finding job [ " + key + " ]. Nested exception: " + e);
        }
    }

    @Override
    public void suspendJob(JobKey key) {
        try {
            throwIfAbsent(key, "suspendJob()");
            scheduler.pauseJob(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed suspending job [ " + key + " ]. Nested exception: " + e);
        }
    }

    @Override
    public void resumeJob(JobKey key) {
        try {
            throwIfAbsent(key, "resumeJob()");
            scheduler.resumeJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed resuming job [ " + key + " ]. Nested exception: " + e);
        }
    }

    private void throwIfAbsent(JobKey key, String method) throws SchedulerException{
        if (!scheduler.checkExists(key)) {
            throw new JobNotFoundException("Job with key = " + key +
                    " not found while proceeding " + method + " method");
        }
    }
}
