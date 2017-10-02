package com.sidorii.scheduler.model.repository;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobKey;

public interface TaskRepository {

    void addTask(Task task, JobKey key);

    void deleteTaskForJob(JobKey key);

    Task getTaskForJob(JobKey key);
}
