package com.sidorii.scheduler.model.job.config;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.SimpleJob;
import com.sidorii.scheduler.model.repository.TaskRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class DefaultJobConfigurerImpl implements JobConfigurer {





    @Override
    public JobDetail buildJob(JobConfiguration configuration) throws ConfigurationException {

        JobDetail detail = newJob(SimpleJob.class)
                .storeDurably(true)
                .usingJobData("type",configuration.getType())
                .build();

        return detail;
    }

    @Override
    public Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException {

        Trigger trigger = newTrigger()
                .withSchedule(buildSchedule(configuration))
                .startAt(configuration.getStartTime())
                .endAt(configuration.getEndTime())
                .build();

        return trigger;
    }


    private ScheduleBuilder<?> buildSchedule(JobConfiguration configuration) throws ConfigurationException {

        if (configuration.getScheduledAt() != null) {
            return cronSchedule(configuration.getScheduledAt())
                    .inTimeZone(configuration.getTimeZone());
        } else if (configuration.getExecuteTimes() != null) {
            return simpleSchedule()
                    .withRepeatCount(configuration.getExecuteTimes())
                    .withIntervalInMilliseconds(calculateRepeatInterval(configuration));
        } else {
            throw new ConfigurationException("Scheduling configuration failed. Check your schedule properties.");
        }
    }


//    Execution interval must be set in request!! (this method shouldn't exist!)
    private long calculateRepeatInterval(JobConfiguration cfg) {
        return (cfg.getEndTime().getTime() - cfg.getStartTime().getTime()) / cfg.getExecuteTimes();
    }
}
