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
public class MultiTaskAdapter implements TaskExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(MultiTaskAdapter.class);

    @Autowired
    private HttpTaskExecutor executor;

    @Override
    public void executeTask(Task task, JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if ((task instanceof HttpTask) && jobDataMap.getString("type").equals("http")) {

            HttpTask httpTask = (HttpTask) task;
            LOGGER.info("Delegate HttpTask [ {} ] to HttpTaskExecutor", httpTask);
            executor.executeTask(httpTask, context);
            return;
        }

        LOGGER.error("No suitable executor found for Task [{}]", task);
    }
}
