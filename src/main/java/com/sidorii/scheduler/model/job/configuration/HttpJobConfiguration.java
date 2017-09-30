package com.sidorii.scheduler.model.job.configuration;

import com.sidorii.scheduler.model.task.HttpTask;

public class HttpJobConfiguration extends JobConfiguration {

    private HttpTask task;

    public HttpTask getTask() {
        return task;
    }

    public void setTask(HttpTask task) {
        this.task = task;
    }

}
