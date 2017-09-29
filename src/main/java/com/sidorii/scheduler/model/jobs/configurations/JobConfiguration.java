package com.sidorii.scheduler.model.jobs.configurations;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.net.URL;
import java.util.Date;
import java.util.TimeZone;

@JsonRootName("body")
public abstract class JobConfiguration {


    private String type;
    private String scheduledAt;
    private Integer executeTimes;
    private Date startTime;
    private Date endTime;
    private TimeZone timeZone;
    private URL callbackUrl;


    //    Getters
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
    public Date getStartTime() {
        return startTime;
    }

    @JsonGetter("end_time")
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
    public void setType(String type) {
        this.type = type;
    }

    @JsonSetter("end_time")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonSetter("start_time")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
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
                '}';
    }
}
