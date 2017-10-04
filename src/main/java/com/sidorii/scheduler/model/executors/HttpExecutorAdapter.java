package com.sidorii.scheduler.model.executors;

import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.model.task.Task;
import com.sidorii.scheduler.model.task.TaskExecutor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpExecutorAdapter implements TaskExecutor{

    private HttpTaskExecutor executor;

    @Autowired
    public HttpExecutorAdapter(HttpTaskExecutor executor) {
        this.executor = executor;
    }


    @Override
    public void executeTask(Task task, JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if (!((task instanceof HttpTask) && jobDataMap.getString("type").equals("http"))) {
            throw new JobExecutionException("Task type not supported for class: " + this.getClass().getName());
        }

        HttpTask httpTask = (HttpTask) task;

        executor.executeTask(httpTask, context);
    }
}
