package com.sidorii.scheduler.executors.impl;

import com.sidorii.scheduler.executors.TaskExecutor;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpExecutorAdapter implements TaskExecutor {

    private HttpTaskExecutor executor;
    private static Logger LOGGER = LoggerFactory.getLogger(HttpExecutorAdapter.class);

    @Autowired
    public HttpExecutorAdapter(HttpTaskExecutor executor) {
        this.executor = executor;
    }


    @Override
    public void executeTask(Task task, JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if (!((task instanceof HttpTask) && jobDataMap.getString("type").equals("http"))) {
            LOGGER.error("Attempt to execute not HttpTask in HttpExecutorAdapter");
            throw new JobExecutionException("Task type not supported for class: {}" + this.getClass().getName());
        }

        HttpTask httpTask = (HttpTask) task;
        LOGGER.info("Delegate HttpTask [ {} ] to HttpTaskExecutor");
        executor.executeTask(httpTask, context);
    }
}
