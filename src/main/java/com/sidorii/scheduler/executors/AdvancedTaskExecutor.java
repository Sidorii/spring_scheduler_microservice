package com.sidorii.scheduler.executors;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface AdvancedTaskExecutor<T extends Task> {

    void executeTask(T task, JobExecutionContext context) throws JobExecutionException;
}
