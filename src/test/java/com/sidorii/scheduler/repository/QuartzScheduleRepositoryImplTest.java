package com.sidorii.scheduler.repository;

import com.sidorii.scheduler.model.job.SimpleJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzScheduleRepositoryImplTest {

    public QuartzScheduleRepositoryImpl repository;

    @Autowired
    public Scheduler scheduler;

    public void setRepository(QuartzScheduleRepositoryImpl repository) {
        this.repository = repository;
    }

    private JobDetail detail;
    private Trigger simpleTrigger;
    private Trigger cronTrigger;
    private JobKey key;


    @Before
    public void setUp() throws SchedulerException {
        repository = new QuartzScheduleRepositoryImpl(scheduler);

        detail = newJob(SimpleJob.class).build();
        simpleTrigger = newTrigger().build();
        cronTrigger = newTrigger().withSchedule(cronSchedule("* * * * * ?")).build();

        key = detail.getKey();
    }


    @Test
    public void testAddJob() throws Exception {

        repository.addJob(detail, cronTrigger);

        JobDetail addedJobDetail = repository.scheduler.getJobDetail(key);
        Trigger addedTrigger = repository.scheduler.getTriggersOfJob(key).get(0);

        assertEquals(detail, addedJobDetail);
        assertEquals(cronTrigger, addedTrigger);
    }

    @Test
    public void testAddJobAnotherCase() throws Exception {

        SchedulerListener listener = mock(SchedulerListener.class);

        repository.addJob(detail, simpleTrigger, listener);

        JobDetail addedJobDetail = repository.scheduler.getJobDetail(key);
        Trigger addedTrigger = repository.scheduler.getTriggersOfJob(key).get(0);
        List<SchedulerListener> listenerList = repository.scheduler.getListenerManager().getSchedulerListeners();

        assertEquals(detail, addedJobDetail);
        assertEquals(simpleTrigger, addedTrigger);
        assertTrue(listenerList.contains(listener));
    }

    @Test
    public void testDeleteJob() throws Exception {
        repository.scheduler.scheduleJob(detail, simpleTrigger);

        repository.deleteJob(key);
        Trigger trigger = repository.scheduler.getTriggersOfJob(key).stream().findFirst().orElse(null);

        assertNull(repository.scheduler.getJobDetail(key));
        assertNull(trigger);
    }

    @Test
    public void testGetJobById() throws Exception {
        repository.scheduler.scheduleJob(detail, cronTrigger);
        JobDetail retrievedDetail = repository.getJobById(key);

        assertEquals(detail, retrievedDetail);
    }

    @Test
    public void testGetTriggersForJob() throws Exception {
        repository.scheduler.scheduleJob(detail, cronTrigger);

        List<? extends Trigger> triggers = repository.getTriggersForJob(key);

        assertEquals(1, triggers.size());
        assertEquals(cronTrigger, triggers.get(0));
    }

    @After
    public void tearDown() throws SchedulerException {
        List<? extends Trigger> triggers = repository.scheduler.getTriggersOfJob(key);

        List<TriggerKey> keys = triggers
                .stream()
                .map(Trigger::getKey)
                .collect(Collectors.toList());
        repository.scheduler.unscheduleJobs(keys);
    }

}