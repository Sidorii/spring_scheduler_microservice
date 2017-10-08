package com.sidorii.scheduler.repository;

import com.sidorii.scheduler.model.exception.JobNotFoundException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("scheduleRepository")
public class QuartzScheduleRepositoryImpl implements QuartzScheduleRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected Scheduler scheduler;



    @Autowired
    public QuartzScheduleRepositoryImpl(Scheduler scheduler) throws SchedulerException {
        LOGGER.debug("Using schedule [{}] in ScheduleRepository ", scheduler.getSchedulerName());
        this.scheduler = scheduler;
    }



    @Override
    public void addJob(JobDetail detail, Trigger trigger) throws SchedulerException {
        JobKey key = detail.getKey();

        if (scheduler.checkExists(key)) {
            LOGGER.error("No such job detail with key", key);
            throw new RuntimeException("Duplicating job while adding new instance to ScheduleService");
        }

        scheduler.scheduleJob(detail, trigger);
        LOGGER.info("Scheduling new job [{}] with trigger [{}]", key.getName(), trigger.getKey().getName());
    }




    @Override
    public void addJob(JobDetail detail, Trigger trigger, SchedulerListener schedulerListener) throws SchedulerException {
        scheduler.getListenerManager().addSchedulerListener(schedulerListener);

        addJob(detail, trigger);
    }




    @Override
    public void deleteJob(JobKey key) throws SchedulerException {
        throwNullOrAbsent(key, "deleteJob()");

        scheduler.deleteJob(key);
        LOGGER.error("Job [{}] successfully removed", key.getName());
    }




    @Override
    public JobDetail getJobById(JobKey key) throws SchedulerException {
        throwNullOrAbsent(key, "getJobById()");
        return scheduler.getJobDetail(key);
    }




    @Override
    public List<? extends Trigger> getTriggersForJob(JobKey key) throws SchedulerException {
        throwNullOrAbsent(key, "getTriggersForJob()");
        return scheduler.getTriggersOfJob(key);
    }




    private void throwNullOrAbsent(JobKey key, String method) throws SchedulerException {

        if (key == null || !scheduler.checkExists(key)) {
            LOGGER.error("Unsupported state when job with key [{}] is not exist. Exception will thrown", key);
            throw new JobNotFoundException("Job with key = " + key +
                    " not found while proceeding " + method + " method");
        }
    }
}
