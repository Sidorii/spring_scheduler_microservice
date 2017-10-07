package com.sidorii.scheduler.controllers;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.job.config.JobDescription;
import com.sidorii.scheduler.service.ScheduleService;
import com.sidorii.scheduler.util.BodyWrapper;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Properties;

@RestController
@RequestMapping("/jobs")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);


    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BodyWrapper<Properties> createTask(
            @Valid @RequestBody BodyWrapper<JobConfiguration> configuration) throws ConfigurationException {


        JobConfiguration jobConfiguration = configuration.getBody();
        LOGGER.debug("Requested JobConfiguration: {} ", jobConfiguration);

        String key = service.addJob(jobConfiguration);

        Properties properties = new Properties();
        properties.setProperty("job_id", key);
        return BodyWrapper.wrap(properties);
    }


    @RequestMapping(value = "/{job_id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public BodyWrapper<JobDescription> jobById(@PathVariable("job_id") String jobId) throws ConfigurationException {
        return BodyWrapper.wrap(service.getJobDescription(jobId));
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{job_id}")
    @ResponseStatus(HttpStatus.OK)
    public Properties deleteTaskById(@PathVariable("job_id") String jobId) {

        service.deleteJob(jobId);

        Properties properties = new Properties();
        properties.setProperty("code", HttpStatus.OK.toString());
        properties.setProperty("message", "job deleted successfully");
        return properties;
    }
}
