package com.sidorii.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidorii.scheduler.model.job.SimpleJob;
import com.sidorii.scheduler.util.BodyWrapper;
import org.junit.Test;
import org.quartz.JobDetail;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.quartz.JobBuilder.newJob;

public class LightweightTest {

    @Test
    public void testJobDetail() {

        JobDetail detail = newJob(SimpleJob.class)
                .build();


        System.out.println(detail.getKey().getName());
        assertNotNull(detail.getKey());
    }

    @Test
    public void testJsonWrapper() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Properties properties = new Properties();
        properties.setProperty("job_key", "1");
        BodyWrapper<Map> bodyWrapper = new BodyWrapper<>(properties);
        System.out.println(mapper.writeValueAsString(bodyWrapper));
    }
}
