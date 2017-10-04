package com.sidorii.scheduler.controllers;

import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.util.BodyWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @RequestMapping(method = RequestMethod.POST)
    public BodyWrapper<String> test(BodyWrapper configuration) {
        System.out.println("In test controller!!!!!!!!!!!!!!!!!!!!");
        return BodyWrapper.wrap("Operation successfully!");
    }
}
