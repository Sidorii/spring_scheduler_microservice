package com.sidorii.scheduler.model.job;

import com.sidorii.scheduler.model.task.TaskExecutor;
import com.sidorii.scheduler.model.repository.TaskService;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@DisallowConcurrentExecution
public class SimpleJob implements Job {


    @Autowired
    private TaskService repository;

    @Autowired
    @Qualifier("httpExecutorAdapter")
    private TaskExecutor taskExecutor;

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setRepository(TaskService repository) {
        this.repository = repository;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Task task = repository.getTaskForJob(jobExecutionContext.getJobDetail().getKey());

        taskExecutor.executeTask(task,jobExecutionContext);

        System.out.println("While execution Job: " + task);
    }
}
