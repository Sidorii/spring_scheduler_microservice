package com.sidorii.scheduler.service;

import com.sidorii.scheduler.model.task.Task;
import org.hibernate.validator.constraints.NotEmpty;
import org.quartz.JobKey;
import org.quartz.TriggerListener;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface TaskService {

    void addTask(@NotNull Task task, @NotNull @NotEmpty String key);

    void deleteTaskForJob( @NotNull @NotEmpty String key);

    Task getTaskForJob(@NotNull @NotEmpty String key);

}
