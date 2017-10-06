package com.sidorii.scheduler.model.job.config;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.sidorii.scheduler.model.task.Task;
import com.sidorii.scheduler.util.JobConfigUtil;
import com.sidorii.scheduler.util.TaskTypeResolver;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;
import java.util.Date;
import java.util.TimeZone;


public class JobConfiguration {


    @JsonProperty("type")
    @NotNull(message = "{conf.type}")
    private String type;

    @NotNull(message = "{conf.scheduledAt.null}")
    @Size(min = 11, message = "{conf.scheduledAt.size}")
    @JsonProperty("scheduled_at")
    private String scheduledAt;

    @JsonProperty("execute_times")
    private Integer executeTimes;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "YYYY-MM-dd hh:mm:ss")
    private Date startTime;

    @Future(message = "{conf.endTime}")
    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date endTime;

    @JsonProperty("time_zone")
    private TimeZone timeZone;

    @JsonProperty("callback_url")
    private URL callbackUrl;

    @NotNull(message = "{conf.task}")
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CUSTOM,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type")
    @JsonTypeIdResolver(TaskTypeResolver.class)
    private Task task;


    public JobConfiguration() {
        startTime = JobConfigUtil.defaultStartDate();
        endTime = JobConfigUtil.defaultEndDate();
        timeZone = JobConfigUtil.defaultTimeZone();
    }


    public Task getTask() {
        return task;
    }


    public void setTask(Task task) {
        this.task = task;
    }

    //    Getters
    public String getType() {
        return type;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public Integer getExecuteTimes() {
        return executeTimes;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public URL getCallbackUrl() {
        return callbackUrl;
    }


    //    Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setCallbackUrl(URL callbackUrl) {
        this.callbackUrl = callbackUrl;
    }


    @Override
    public String toString() {
        return "JobConfiguration{" +
                "type='" + type + '\'' +
                ", scheduledAt='" + scheduledAt + '\'' +
                ", executeTimes=" + executeTimes +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeZone=" + timeZone +
                ", callbackUrl=" + callbackUrl +
                ", task=" + task +
                '}';
    }
}
