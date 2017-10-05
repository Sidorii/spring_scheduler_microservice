package com.sidorii.scheduler.service;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobKey;
import org.quartz.TriggerListener;

public interface TaskService {

    void addTask(Task task, JobKey key);

    void deleteTaskForJob(JobKey key);

    Task getTaskForJob(JobKey key);

}
