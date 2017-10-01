package com.sidorii.scheduler.model.job;

import com.sidorii.scheduler.model.task.HttpTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


public class HttpJob implements Job {

    private HttpTask task;

    private RestTemplate template = new RestTemplate();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //should use RequestEntity<>()
//        switch (task.getMethod()){
//            case GET:
//                template.getForEntity(...)
//        }

    }

    public void setTask(HttpTask task) {
        this.task = task;
    }
}
