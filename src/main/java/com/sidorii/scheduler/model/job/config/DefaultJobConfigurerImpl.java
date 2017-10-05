package com.sidorii.scheduler.model.job.config;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.SimpleJob;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class DefaultJobConfigurerImpl implements JobConfigurer {


    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJobConfigurerImpl.class);


    @Override
    public JobDetail buildJob(JobConfiguration configuration) throws ConfigurationException {

        JobDataMap data = new JobDataMap();
        data.put("time_zone", configuration.getTimeZone().getID());
        data.put("type", configuration.getType());
        data.put("callback_url", configuration.getCallbackUrl().toString());

        JobDetail detail = newJob(SimpleJob.class)
                .usingJobData(data)
                .build();

        LOGGER.info("Building JobDetail successfully over. JobDetail: {}", detail);

        return detail;
    }

    @Override
    public Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException {

        Trigger trigger = newTrigger()
                .withSchedule(buildSchedule(configuration))
                .startAt(configuration.getStartTime())
                .endAt(configuration.getEndTime())
                .build();

        LOGGER.info("Building Trigger successfully over. Trigger: {}", trigger);
        return trigger;
    }



    @Override
    public JobDescription buildJobDescription(JobDetail detail, Trigger trigger, Task task) {
        JobDescription description = new JobDescription();

        description.setTask(task);

        configureDescriptionByJobDetail(description, detail);
        configureDescriptionByTriggerType(description,trigger);

        return description;
    }


    private ScheduleBuilder<?> buildSchedule(JobConfiguration configuration) throws ConfigurationException {

        if (configuration.getScheduledAt() != null) {
            LOGGER.info("Cron schedule building...");
            return cronSchedule(configuration.getScheduledAt())
                    .inTimeZone(configuration.getTimeZone());
        } else if (configuration.getExecuteTimes() != null) {
            LOGGER.info("Simple schedule building...");
            return simpleSchedule()
                    .withRepeatCount(configuration.getExecuteTimes())
                    .withIntervalInMilliseconds(calculateRepeatInterval(configuration));
        } else {
            LOGGER.error("Configuration schedule failed. Wrong state of scheduledAt and executeTime parameters " +
                    "because they are null.");
            throw new ConfigurationException("Scheduling configuration failed. Check your schedule properties." +
                    "Scheduled at: " + configuration.getScheduledAt() + " , Execute times: " + configuration.getExecuteTimes());
        }
    }

    private void configureDescriptionByTriggerType(JobDescription description, Trigger trigger) {

        if (trigger == null) {
            return;
        }

        description.setLastRunAt(trigger.getPreviousFireTime());
        description.setNextRunAt(trigger.getNextFireTime());
        description.setStartTime(trigger.getStartTime());
        description.setEndTime(trigger.getEndTime());

        if (trigger instanceof SimpleTrigger) {
            description.setExecuteTimes(((SimpleTrigger) trigger).getTimesTriggered());
        } else if (trigger instanceof CronTrigger) {
            description.setScheduledAt(((CronTrigger) trigger).getCronExpression());
        }
    }

    private void configureDescriptionByJobDetail(JobDescription description, JobDetail detail) {
        JobDataMap dataMap = detail.getJobDataMap();

        description.setJobId(detail.getKey().getName());
        description.setTimeZone(TimeZone.getTimeZone(dataMap.getString("time_zone")));
        description.setType(dataMap.getString("type"));
        Integer code = dataMap.getString("code") != null ?
                Integer.parseInt(dataMap.getString("code")) : null;
        description.setLastRunResult(code, dataMap.getString("body"));

        try {
            description.setCallbackUrl(new URL(dataMap.getString("callback_url")));
        } catch (MalformedURLException e) {
            LOGGER.error("Incorrect callback URL: {}", e.getMessage());
        }
    }


    //    Execution interval must be set in request!! (this method shouldn't exist!)
    private long calculateRepeatInterval(JobConfiguration cfg) {
        return (cfg.getEndTime().getTime() - cfg.getStartTime().getTime()) / cfg.getExecuteTimes();
    }
}
