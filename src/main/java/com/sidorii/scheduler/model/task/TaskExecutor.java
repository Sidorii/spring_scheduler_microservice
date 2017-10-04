package com.sidorii.scheduler.model.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface TaskExecutor {

//    TODO: Implement me (and my adapter(-s))

    void executeTask(Task task, JobExecutionContext context) throws JobExecutionException;
}