package com.sidorii.scheduler.model.job;

import com.sidorii.scheduler.executors.TaskExecutor;
import com.sidorii.scheduler.model.task.Task;
import com.sidorii.scheduler.repository.TaskRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SimpleJob implements Job {


    @Autowired
    @Qualifier("jdbcTaskRepository")
    private TaskRepository repository;

    @Autowired
    @Qualifier("httpExecutorAdapter")
    private TaskExecutor taskExecutor;


    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setRepository(TaskRepository repository) {
        this.repository = repository;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Task task = repository.getTaskForJob(jobExecutionContext.getJobDetail().getKey());

        taskExecutor.executeTask(task, jobExecutionContext);
    }
}
