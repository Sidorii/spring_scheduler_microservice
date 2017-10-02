package com.sidorii.scheduler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.util.BodyWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/jobs")
public class ScheduleController {


    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BodyWrapper<Properties> createHttpTask(@RequestBody BodyWrapper<JobConfiguration> configuration) {

        //TODO : implement me

        Properties properties = new Properties();
        properties.setProperty("job_id", "1");

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
