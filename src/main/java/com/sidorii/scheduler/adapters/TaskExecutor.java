package com.sidorii.scheduler.adapters;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobExecutionContext;

public interface TaskExecutor {

//    TODO: Implement me (and my adapter(-s))

    void executeJob(Task task, JobExecutionContext context);
}