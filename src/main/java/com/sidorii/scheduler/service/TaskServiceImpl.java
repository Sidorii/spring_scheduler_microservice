package com.sidorii.scheduler.service;

import com.sidorii.scheduler.model.exception.TaskException;
import com.sidorii.scheduler.model.task.Task;
import com.sidorii.scheduler.repository.TaskRepository;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("jdbcTaskRepository")
    private TaskRepository repository;


    @Override
    public void addTask(Task task, String key) {

        try {
            repository.addTask(task, key);

        } catch (DuplicateKeyException e) {
            LOGGER.error("Attempt to save task with duplicated key is failed.");
            throw new TaskException("Task with key = " + key + " already exists");
        }
    }

    @Override
    public void deleteTaskForJob(String key) {
        repository.deleteTaskForJob(key);
        LOGGER.info("Task with key [{}] successfully deleted", key);
    }

    @Override
    public Task getTaskForJob(String key) {

        try {
            return repository.getTaskForJob(key);

        } catch (DataAccessException e) {
            LOGGER.error("Can't find Task with by key = {}", key);
            throw new TaskException("Task with key = " + key + " does not exists");
        }
    }
}
