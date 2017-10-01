package com.sidorii.scheduler.controllers;

import com.sidorii.scheduler.model.job.configuration.JobConfiguration;
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
    public BodyWrapper<Map> createHttpTask(@RequestBody BodyWrapper<JobConfiguration> configuration) {

        Properties properties = new Properties();
        properties.setProperty("job_id", "1");
        return BodyWrapper.wrap(properties);
    }


    @RequestMapping(value = "/{job_id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public BodyWrapper<?> jobById(@PathVariable("job_id") String jobId) {
        throw new RuntimeException("Implement me");
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{job_id}")
    @ResponseStatus(HttpStatus.OK)
    public Properties deleteTaskById(@PathVariable("job_id") String jobId) {

        //implementation here

        Properties properties = new Properties();
        properties.setProperty("code", HttpStatus.OK.toString());
        properties.setProperty("message", "job deleted successfully");
        return properties;
    }
}
