package com.sidorii.scheduler.model.service;

import com.sidorii.scheduler.model.repository.ScheduleRepository;
import com.sidorii.scheduler.model.repository.TaskRepository;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    @Qualifier("scheduleRepository")
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TaskRepository repository;


    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void setRepository(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public void addJob(JobDetail detail, Task task, Trigger trigger) {
        try {
            repository.addTask(task, detail.getKey());
            scheduleRepository.addJob(detail,trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Attempt to schedule job [ " + detail.getKey() + " ] with trigger [ "
                    + trigger.getKey() + " ] is failed");
        }
    }

    @Transactional
    @Override
    public void deleteJob(JobKey key) {
        try {
            scheduleRepository.deleteJob(key);
            repository.deleteTaskForJob(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed deleting job [" + key + " ]. Nested exception: " + e);
        }
    }


    @Override
    public JobDetail getJobById(JobKey key) {
        try {
            return scheduleRepository.getJobById(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed finding job [ " + key + " ]. Nested exception: " + e);
        }
    }

    @Transactional
    @Override
    public void suspendJob(JobKey key) {
        try {
            scheduleRepository.suspendJob(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed suspending job [ " + key + " ]. Nested exception: " + e);
        }
    }

    @Transactional
    @Override
    public void resumeJob(JobKey key) {
        try {
            scheduleRepository.resumeJob(key);

        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed resuming job [ " + key + " ]. Nested exception: " + e);
        }
    }
}
