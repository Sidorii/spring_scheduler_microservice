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
import java.util.Date;
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

        TimeZone timeZone = configuration.getTimeZone();

        JobDataMap data = new JobDataMap() {
            {
                put(TIME_ZONE, timeZone.getID());
                put(TYPE, configuration.getType());
                put(CODE,0);
                put(BODY, null);

                if (configuration.getCallbackUrl() != null) {
                    put(CALLBACK_URL, configuration.getCallbackUrl().toString());
                }
            }
        };


        JobDetail detail = newJob(SimpleJob.class)
                .usingJobData(data)
                .build();

        LOGGER.info("Building JobDetail successfully over. JobDetail: {}", detail);

        return detail;
    }





    @Override
    public Trigger buildTrigger(JobConfiguration configuration) throws ConfigurationException {

        Date startDate = configuration.getStartTime();
        Date endDate = configuration.getEndTime();


        Trigger trigger = newTrigger()
                .withSchedule(buildSchedule(configuration))
                .startAt(startDate)
                .endAt(endDate)
                .build();

        LOGGER.info("Building Trigger successfully over. Trigger: {}", trigger.getKey());
        return trigger;
    }





    @Override
    public JobDescription buildJobDescription(JobDetail detail, Trigger trigger, Task task) {
        JobDescription description = new JobDescription();

        description.setTask(task);

        configureDescriptionByJobDetail(description, detail);
        configureDescriptionByTriggerType(description, trigger);

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
        description.setTimeZone(TimeZone.getTimeZone(dataMap.getString(TIME_ZONE)));
        description.setType(dataMap.getString(TYPE));

        Integer code = dataMap.containsKey(CODE) ?
                dataMap.getInt(CODE) : null;

        description.setLastRunResult(code, dataMap.getString(BODY));

        try {
            if (dataMap.containsKey(CALLBACK_URL)) {
                description.setCallbackUrl(new URL(dataMap.getString(CALLBACK_URL)));
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Incorrect callback URL: {}", e.getMessage());
        }
    }





    //    Execution interval must be set in request!! (this method shouldn't exist!)
    private long calculateRepeatInterval(JobConfiguration cfg) {
        return (cfg.getEndTime().getTime() - cfg.getStartTime().getTime()) / cfg.getExecuteTimes();
    }
}
