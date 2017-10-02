package com.sidorii.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.quartz.JobBuilder.newJob;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerServiceApplicationTests {

    @Test
    public void contextLoads() {
    }
}
