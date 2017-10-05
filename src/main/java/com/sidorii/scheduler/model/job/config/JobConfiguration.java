package com.sidorii.scheduler.model.job.config;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.sidorii.scheduler.model.task.Task;
import com.sidorii.scheduler.util.TaskTypeResolver;

import java.net.URL;
import java.util.Date;
import java.util.TimeZone;

public class JobConfiguration {


    private String type;
    private String scheduledAt;
    private Integer executeTimes;
    private Date startTime;
    private Date endTime;
    private TimeZone timeZone;
    private URL callbackUrl;


    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CUSTOM,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type")
    @JsonTypeIdResolver(TaskTypeResolver.class)
    private Task task;


    public Task getTask() {
        return task;
    }


    public void setTask(Task task) {
        this.task = task;
    }

    //    Getters
    @JsonGetter("type")
    public String getType() {
        return type;
    }

    @JsonGetter("scheduled_at")
    public String getScheduledAt() {
        return scheduledAt;
    }

    @JsonGetter("execute_times")
    public Integer getExecuteTimes() {
        return executeTimes;
    }

    @JsonGetter("start_time")
    @JsonFormat(pattern = "YYYY-MM-dd hh:mm:ss", timezone = "Europe/Kiev")
    public Date getStartTime() {
        return startTime;
    }

    @JsonGetter("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Europe/Kiev")
    public Date getEndTime() {
        return endTime;
    }

    @JsonGetter("time_zone")
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @JsonGetter("callback_url")
    public URL getCallbackUrl() {
        return callbackUrl;
    }


    //    Setters
    @JsonSetter("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonSetter("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Europe/Kiev")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonSetter("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Europe/Kiev")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonSetter("execute_times")
    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    @JsonSetter("scheduled_at")
    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    @JsonSetter("time_zone")
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @JsonSetter("callback_url")
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
