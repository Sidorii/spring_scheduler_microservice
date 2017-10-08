package com.sidorii.scheduler.repository;

import com.sidorii.scheduler.model.task.Task;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;


public interface TaskRepository {

    void addTask(Task task, String key) throws DuplicateKeyException;

    void deleteTaskForJob(String key);

    Task getTaskForJob(String key) throws DataAccessException;
}
