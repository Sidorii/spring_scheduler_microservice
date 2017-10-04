package com.sidorii.scheduler.model.executors;

import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.task.AdvancedTaskExecutor;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.model.task.TaskExecutor;
import com.sidorii.scheduler.util.BodyWrapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class HttpTaskExecutor implements AdvancedTaskExecutor<HttpTask>{

    private RestTemplate template;

    public HttpTaskExecutor() {
        template = new RestTemplate();
    }

    @Override
    public void executeTask(HttpTask task, JobExecutionContext context) {

        try {
            CustomHttpHeaders httpHeaders = task.getHeaders();
            HttpMethod method = task.getMethod();
            URI uri = task.getUrl().toURI();
            String data = task.getData();

            RequestEntity<BodyWrapper<String>> request = new RequestEntity<>(BodyWrapper.wrap(data),method, uri);
            ResponseEntity<String> response =
                    template.exchange(request, String.class);

            System.out.println(response.getBody());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
