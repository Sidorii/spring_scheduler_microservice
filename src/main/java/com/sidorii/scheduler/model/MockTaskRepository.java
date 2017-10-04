package com.sidorii.scheduler.model;

import com.sidorii.scheduler.model.repository.TaskService;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MockTaskRepository implements TaskService {


    public Map<JobKey, Task> repo = new HashMap<>();

    @Override
    public void addTask(Task task, JobKey key) {
        repo.put(key, task);
    }

    @Override
    public void deleteTaskForJob(JobKey key) {
        repo.remove(key);
    }

    @Override
    public Task getTaskForJob(JobKey key) {
        return repo.get(key);
    }
}
