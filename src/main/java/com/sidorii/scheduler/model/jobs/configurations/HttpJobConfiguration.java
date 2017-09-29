package com.sidorii.scheduler.model.jobs.configurations;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sidorii.scheduler.model.tasks.HttpTask;

@JsonRootName("body")
public class HttpJobConfiguration extends JobConfiguration {

    private HttpTask task;

    public HttpTask getTask() {
        return task;
    }

    public void setTask(HttpTask task) {
        this.task = task;
    }

}
