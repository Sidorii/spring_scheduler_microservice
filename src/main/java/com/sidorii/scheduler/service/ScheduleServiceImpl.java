package com.sidorii.scheduler.service;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.job.config.JobConfigurer;
import com.sidorii.scheduler.model.job.config.JobDescription;
import com.sidorii.scheduler.model.task.Task;
import com.sidorii.scheduler.repository.ScheduleRepository;
import org.quartz.*;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    @Qualifier("scheduleRepository")
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TaskService taskService;


    @Autowired
    private JobConfigurer configurer;


    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);


    @Transactional
    @Override
    public String addJob(JobConfiguration configuration) throws ConfigurationException {

        if (configuration == null) {
            LOGGER.error("JobConfiguration is null");
            throw new ConfigurationException("Cannot configure Job while configuration = null");
        }

        JobDetail detail = null;
        Trigger trigger = null;
        Task task;

        try {
            detail = configurer.buildJob(configuration);
            trigger = configurer.buildTrigger(configuration);
            task = configuration.getTask();
            taskService.addTask(task, detail.getKey());
            SchedulerListener listener = new TaskSchedulerListener();
            scheduleRepository.addJob(detail, trigger, listener);
            LOGGER.debug("New instance added: Job Detail [{}], Trigger [{}], Listener [{}]",
                    detail.getKey().getName(), trigger.getKey().getName(), listener);
            return detail.getKey().getName();
        } catch (SchedulerException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Attempt to schedule job [ " + detail.getKey() + " ] with trigger [ "
                    + trigger.getKey() + " ] is failed");
        }
    }

    @Transactional
    @Override
    public void deleteJob(String key) {
        JobKey jobKey = new JobKey(key);
        try {
            scheduleRepository.deleteJob(jobKey);
            taskService.deleteTaskForJob(jobKey);

        } catch (SchedulerException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Failed deleting job [" + key + " ]. Nested exception: " + e);
        }
    }


    @Transactional
    @Override
    public JobDescription getJobDescription(String key) throws ConfigurationException {
        JobKey jobKey = new JobKey(key);

        try {
            JobDetail detail = scheduleRepository.getJobById(jobKey);
            Trigger trigger = scheduleRepository.getTriggersForJob(jobKey).get(0);
            Task task = taskService.getTaskForJob(jobKey);

            return configurer.buildJobDescription(detail, trigger, task);

        } catch (SchedulerException e) {
            LOGGER.error(e.toString());
            throw new ConfigurationException("Configuration description failed, cause.", e);
        }
    }

    protected class TaskSchedulerListener extends SchedulerListenerSupport {


        @Override
        public void triggerFinalized(Trigger trigger) {
            JobKey jobKey = trigger.getJobKey();
            if (jobKey == null) {
                LOGGER.warn("JobKey in TaskListener is null");
                return;
            }
            LOGGER.debug("JobKey in TaskListener is: {}", jobKey.getName());

            taskService.deleteTaskForJob(jobKey);
        }

    }
}
