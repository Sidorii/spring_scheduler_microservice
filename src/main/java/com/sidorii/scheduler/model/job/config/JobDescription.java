package com.sidorii.scheduler.model.job.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sidorii.scheduler.model.response.ResponseBody;

import java.util.Date;

public class JobDescription extends JobConfiguration {

    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("next_run_at")
    @JsonFormat(pattern = "YYYY-MM-dd hh:mm:ss")
    private Date nextRunAt;
    @JsonProperty("last_run_at")
    @JsonFormat(pattern = "YYYY-MM-dd hh:mm:ss")
    private Date lastRunAt;
    @JsonProperty("last_run_result")
    private ResponseBody lastRunResult;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Date getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(Date nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public Date getLastRunAt() {
        return lastRunAt;
    }

    public void setLastRunAt(Date lastRunAt) {
        this.lastRunAt = lastRunAt;
    }

    public ResponseBody getLastRunResult() {
        return lastRunResult;
    }

    public void setLastRunResult(Integer code, String body) {
        this.lastRunResult = new ResponseBody(code, body);
    }

    public void setLastRunResult(ResponseBody body) {
        this.lastRunResult = body;
    }

}
