package com.sidorii.scheduler.repository;

import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobKey;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

public interface TaskRepository {

    void addTask(Task task, JobKey key) throws DuplicateKeyException;

    void deleteTaskForJob(JobKey key);

    Task getTaskForJob(JobKey key) throws DataAccessException;
}
