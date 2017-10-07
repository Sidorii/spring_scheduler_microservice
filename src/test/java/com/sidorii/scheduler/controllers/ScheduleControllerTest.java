package com.sidorii.scheduler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.exception.JobNotFoundException;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.job.config.JobDescription;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.service.ScheduleService;
import com.sidorii.scheduler.util.BodyWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleControllerTest {

    private JobConfiguration jobConfiguration;
    private ObjectMapper mapper;
    private String json;

    @Autowired
    private ScheduleController controller;

    @MockBean
    private ScheduleService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws MalformedURLException, JsonProcessingException {
        mapper = new ObjectMapper();
        jobConfiguration = new JobConfiguration();
        mockMvc = standaloneSetup(controller)
                .build();


        HttpTask task = new HttpTask();
        Date time = Date.from(LocalDateTime.of(2018, 2, 1, 12, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        task.setUrl(new URL("http://test.com"));
        task.setMethod(HttpMethod.PUT);
        task.setData("test");

        CustomHttpHeaders customHttpHeaders = new CustomHttpHeaders();
        customHttpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        customHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
        customHttpHeaders.setAuthorization("test authority");
        task.setHeaders(customHttpHeaders);


        jobConfiguration.setType("http");
        jobConfiguration.setTask(task);
        jobConfiguration.setEndTime(time);
        jobConfiguration.setStartTime(time);
        jobConfiguration.setScheduledAt("0/10 * * * * ?");
        jobConfiguration.setExecuteTimes(10);
        jobConfiguration.setCallbackUrl(new URL("http://test.com"));
        jobConfiguration.setTimeZone(TimeZone.getTimeZone("UA"));

        json = mapper.writeValueAsString(BodyWrapper.wrap(jobConfiguration));
    }


    @Test
    public void testCreateJobFromFullRequest() throws Exception {

        String jobKey = "1";
        String expectedJson = "{\"body\": {\"job_id\": \"1\"}}";


        when(service.addJob(any(JobConfiguration.class)))
                .thenReturn(jobKey);


        mockMvc.perform(post("/jobs")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status()
                        .isCreated())
                .andExpect(content()
                        .json(expectedJson));

    }

    @Test
    public void testPutDefaultValueIfNotPresent() throws Exception {
        jobConfiguration.setEndTime(null);
        jobConfiguration.setStartTime(null);
        jobConfiguration.setTimeZone(null);
        jobConfiguration.setCallbackUrl(null);
        jobConfiguration.setExecuteTimes(null);

        HttpTask emptyDefaultTask = new HttpTask();

        CustomHttpHeaders newHeaders = new CustomHttpHeaders();
        newHeaders.setAuthorization("some value");

        emptyDefaultTask.setHeaders(newHeaders);
        emptyDefaultTask.setMethod(HttpMethod.POST);
        emptyDefaultTask.setUrl(new URL("http://localhost:8080/test"));
        emptyDefaultTask.setData(null);

        jobConfiguration.setTask(emptyDefaultTask);

        json = mapper.writeValueAsString(BodyWrapper.wrap(jobConfiguration));


        String jobKey = "1";
        String expectedJson = "{\"body\": {\"job_id\": \"1\"}}";


        when(service.addJob(any(JobConfiguration.class)))
                .thenReturn(jobKey);


        mockMvc.perform(post("/jobs")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content()
                            .json(expectedJson));

    }


    @Test
    public void testGetExistedJob() throws Exception {

        JobDescription description = new JobDescription();
        description.setJobId("test-job-id");

        String expectedJson = mapper.writeValueAsString(BodyWrapper.wrap(description));

        when(service.getJobDescription(any(String.class)))
                .thenReturn(description);

        mockMvc.perform(get("/jobs/{job_key}",description.getJobId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isFound())
                .andExpect(content()
                        .json(expectedJson));
    }


    @Test(expected = NestedServletException.class)
    public void testGetJobByWrongKey() throws Exception {

        String wrongKey = "object-with-this-key-is-not-exists";

        when(service.getJobDescription(any(String.class)))
                .thenThrow(new JobNotFoundException());


        mockMvc.perform(get("/jobs/{job_key}", wrongKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status()
                .isNotFound());
    }

    @Test
    public void testDeleteJob() throws Exception {

        String key = "some job key";


        String resultJson = "{\"code\": \"200\" ,\"message\": \"job deleted successfully\"}";

        mockMvc.perform(delete("/jobs/{job_key}", key)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
         .andExpect(status()
                .isOk())
         .andExpect(content()
                .json(resultJson));

        verify(service, atLeastOnce()).deleteJob(any(String.class));
    }

    @Test(expected = NestedServletException.class)
    public void testDeleteJobByWrongKey() throws Exception {

        doThrow(new JobNotFoundException()).when(service).deleteJob(any());

        mockMvc.perform(delete("/jobs/{job_key}","some key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

