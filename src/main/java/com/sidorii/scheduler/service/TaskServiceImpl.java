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
    public void addTask(Task task, JobKey key) {

        if (task == null || key == null) {
            LOGGER.error("Not supported null parameter for addTask({}, {}) method", task, key);
            throw new RuntimeException("Cannot add Task in repository, while instance or key is null");
        }
        try {
            repository.addTask(task, key);

        } catch (DuplicateKeyException e) {
            LOGGER.error("Attempt to save task with duplicated key is failed.");
            throw new TaskException("Task with key = " + key.getName() + " already exists");
        }
    }

    @Override
    public void deleteTaskForJob(JobKey key) {
        repository.deleteTaskForJob(key);
        LOGGER.info("Task with key [{}] successfully deleted", key.getName());
    }

    @Override
    public Task getTaskForJob(JobKey key) {

        if (key == null) {
            return null;
        }

        try {
            return repository.getTaskForJob(key);

        } catch (DataAccessException e) {
            LOGGER.error("Can't find Task with by key = {}", key.getName());
            throw new TaskException("Task with key = " + key.getName() + " does not exists");
        }
    }
}
