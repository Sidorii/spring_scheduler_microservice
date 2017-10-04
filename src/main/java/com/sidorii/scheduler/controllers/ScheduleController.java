package com.sidorii.scheduler.controllers;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.job.config.JobConfigurer;
import com.sidorii.scheduler.model.service.ScheduleService;
import com.sidorii.scheduler.util.BodyWrapper;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
@RequestMapping("/jobs")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    public void setService(ScheduleService service) {
        this.service = service;
    }

    @Autowired
    private JobConfigurer configurer;

    public void setConfigurer(JobConfigurer configurer) {
        this.configurer = configurer;
    }


    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BodyWrapper<Properties> createHttpTask(@RequestBody BodyWrapper<JobConfiguration> configuration) {

        JobConfiguration jobConfiguration = configuration.getBody();

        System.out.println(jobConfiguration);
        Properties properties = new Properties();

        try {
            JobDetail detail = configurer.buildJob(jobConfiguration);
            Trigger trigger = configurer.buildTrigger(jobConfiguration);
            service.addJob(detail,jobConfiguration.getTask(),trigger);

            properties.setProperty("job_id", "1");
        } catch (ConfigurationException e) {
            e.printStackTrace();
            properties.setProperty("error", "job config error");
        }

        return BodyWrapper.wrap(properties);
    }


    @RequestMapping(value = "/{job_id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public BodyWrapper<?> jobById(@PathVariable("job_id") String jobId) {
        //TODO : implement me
        return null;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{job_id}")
    @ResponseStatus(HttpStatus.OK)
    public Properties deleteTaskById(@PathVariable("job_id") String jobId) {

        //TODO : implement me

        Properties properties = new Properties();
        properties.setProperty("code", HttpStatus.OK.toString());
        properties.setProperty("message", "job deleted successfully");
        return properties;
    }
}
