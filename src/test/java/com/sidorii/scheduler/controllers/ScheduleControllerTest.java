package com.sidorii.scheduler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.job.config.JobConfiguration;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.util.BodyWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private ScheduleController controller;

    @Before
    public void setUp() throws MalformedURLException, JsonProcessingException {
        mapper = new ObjectMapper();
        controller = new ScheduleController();
        jobConfiguration = new JobConfiguration();

        HttpTask task = new HttpTask();
        Date time = Date.from(LocalDateTime.of(2017, 2, 1, 12, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
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
    public void testCreateHttpTask() throws Exception {

        MockMvc mockMvc = standaloneSetup(controller).build();

        String resultJson = "{\"body\":{\"job_id\":\"1\"}}";

        mockMvc.perform(post("/jobs")
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .json(resultJson));
    }

    @Test
    public void testGetTask() throws Exception {

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(delete("/jobs/1"))
                .andExpect(content().json("{\"code\":\"200\",\"message\":\"job deleted successfully\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testJsonParser() throws IOException {

        BodyWrapper<JobConfiguration> configurationBodyWrapper =
                mapper.readValue(json, new TypeReference<BodyWrapper<JobConfiguration>>() {
                });

        System.out.println(configurationBodyWrapper.getBody());
    }

    @Test
    public void testTest() throws IOException {
        String jobJ = mapper.writeValueAsString(jobConfiguration);

        System.out.println(jobJ);

        JobConfiguration configuration = mapper.readValue(jobJ, JobConfiguration.class);

        System.out.println(configuration);
    }
}