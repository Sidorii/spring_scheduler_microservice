package com.sidorii.scheduler.model.job;

import com.sidorii.scheduler.model.repository.TaskRepository;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@DisallowConcurrentExecution
public class SimpleJob implements Job {


    @Autowired
    private TaskRepository repository;

    public void setRepository(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Task task = repository.getTaskForJob(jobExecutionContext.getJobDetail().getKey());

        System.out.println("While execution Job: " + task);
    }
}
