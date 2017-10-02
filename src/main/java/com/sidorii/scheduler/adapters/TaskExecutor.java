package com.sidorii.scheduler.adapters;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobExecutionContext;

public interface TaskExecutor {

    void executeJob(Task task, JobExecutionContext context);
}