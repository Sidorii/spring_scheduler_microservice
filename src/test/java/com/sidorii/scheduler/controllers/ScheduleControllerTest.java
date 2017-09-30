package com.sidorii.scheduler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.job.configuration.HttpJobConfiguration;
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

    private HttpJobConfiguration jobConfiguration;
    private ObjectMapper mapper;
    private String json;
    private ScheduleController controller;

    @Before
    public void setUp() throws MalformedURLException, JsonProcessingException {
        mapper = new ObjectMapper();
        controller = new ScheduleController();
        jobConfiguration = new HttpJobConfiguration();

        HttpTask task = new HttpTask();
        Date time = Date.from(LocalDateTime.of(2017, 2, 1, 12, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        task.setUrl(new URL("http://result.com"));
        task.setMethod(HttpMethod.PUT);
        task.setData("test");

        CustomHttpHeaders customHttpHeaders = new CustomHttpHeaders();
        customHttpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        customHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
        customHttpHeaders.setAuthorization("sdvsdv");
        task.setHeaders(customHttpHeaders);

        jobConfiguration.setType("http");
        jobConfiguration.setTask(task);
        jobConfiguration.setEndTime(time);
        jobConfiguration.setStartTime(time);
        jobConfiguration.setScheduledAt("test");
        jobConfiguration.setExecuteTimes(10);
        jobConfiguration.setCallbackUrl(new URL("http://test.com"));
        jobConfiguration.setTimeZone(TimeZone.getTimeZone("UA"));

        json = mapper.writeValueAsString(new BodyWrapper<>(jobConfiguration));
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

}