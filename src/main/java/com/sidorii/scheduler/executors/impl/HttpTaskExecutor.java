package com.sidorii.scheduler.executors.impl;

import com.sidorii.scheduler.executors.AdvancedTaskExecutor;
import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.task.HttpTask;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
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

        try {
            CustomHttpHeaders httpHeaders = task.getHeaders();
            HttpMethod method = task.getMethod();
            URI uri = task.getUrl().toURI();
            String data = task.getData();

            RequestEntity<String> request = new RequestEntity<>(data,httpHeaders,method, uri);
            LOGGER.info("Sending request [{}] on URI: {}",request,uri);
            ResponseEntity<String> response =
                    template.exchange(request, String.class);

            LOGGER.info("Response body after execution: {} ", response.getBody());
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }

    }
}
