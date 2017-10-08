package com.sidorii.scheduler.executors.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sidorii.scheduler.executors.AdvancedTaskExecutor;
import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.job.config.JobConfigurer;
import com.sidorii.scheduler.model.response.ResponseBody;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.util.BodyWrapper;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class HttpTaskExecutor implements AdvancedTaskExecutor<HttpTask> {

    private RestTemplate template;

    public HttpTaskExecutor() {
        template = new RestTemplate();
    }

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void executeTask(HttpTask task, JobExecutionContext context) {

            JobDataMap jobData = context.getJobDetail().getJobDataMap();

            try {

            CustomHttpHeaders httpHeaders = task.getHeaders();
            HttpMethod method = task.getMethod();
            URI uri = task.getUrl().toURI();
            String data = task.getData();

            RequestEntity<String> request = new RequestEntity<>(data, httpHeaders, method, uri);

            LOGGER.info("Sending request [{}] on URI: {}", request, uri);

            ResponseEntity<String> response =
                    template.exchange(request, String.class);

            LOGGER.info("Response body after execution: {} ", response.getBody());


            jobData.replace(JobConfigurer.CODE, response.getStatusCodeValue());
            jobData.replace(JobConfigurer.BODY, response.getBody());


            if (jobData.containsKey(JobConfigurer.CALLBACK_URL)) {
                String callbackUrl = jobData.getString(JobConfigurer.CALLBACK_URL);
                HttpStatus status = sendCallback(callbackUrl, response, context);
                LOGGER.info("Callback successfully sent while task executing. Response status code: {}", status);
            }

            LOGGER.debug("Task [{}] executed successfully", context.getJobDetail().getKey());
        } catch (Exception e) {
                jobData.put(JobConfigurer.CODE, HttpStatus.BAD_REQUEST);
                jobData.putIfAbsent(JobConfigurer.BODY, e.getMessage());
            LOGGER.error(e.getMessage());
        }

    }


    private HttpStatus sendCallback(String url, ResponseEntity<String> responseEntity, JobExecutionContext context) {

        String jobKey = context.getJobDetail().getKey().getName();
        String body = responseEntity.getBody();
        ResponseBody responseBody = new ResponseBody(responseEntity.getStatusCodeValue(), body);

        ResponseEntity<String> response =
                template.postForEntity(url, BodyWrapper.wrap(new CallbackRequest(jobKey, responseBody)), String.class);

        return response.getStatusCode();
    }


    protected class CallbackRequest {

        @JsonProperty("job_id")
        protected String jobId;
        protected ResponseBody result;


        @JsonCreator
        protected CallbackRequest(@JsonProperty("job_id") String jobId, ResponseBody result) {
            this.jobId = jobId;
            this.result = result;
        }

        public String getJobId() {
            return jobId;
        }

        public ResponseBody getResult() {
            return result;
        }
    }
}
