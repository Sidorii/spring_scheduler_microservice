package com.sidorii.scheduler.executors;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface TaskExecutor {

    void executeTask(Task task, JobExecutionContext context) throws JobExecutionException;
}