package com.sidorii.scheduler.controllers;

import com.sidorii.scheduler.model.jobs.configurations.HttpJobConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ScheduleController {


    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpJobConfiguration httpTask(@RequestBody HttpJobConfiguration configuration) {
        System.out.println("In controller: " + configuration);
        return configuration;
    }

}
