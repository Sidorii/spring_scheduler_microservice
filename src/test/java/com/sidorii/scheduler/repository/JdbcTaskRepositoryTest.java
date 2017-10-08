package com.sidorii.scheduler.repository;

import com.sidorii.scheduler.model.CustomHttpHeaders;
import com.sidorii.scheduler.model.exception.TaskException;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.model.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTaskRepositoryTest {


    @Autowired
    public JdbcTaskRepository repository;

    private HttpTask task;
    private String key;

    @Before
    public void setUp() throws MalformedURLException {

        task = new HttpTask();
        task.setData("Test DB data");

        CustomHttpHeaders headers = new CustomHttpHeaders();
        headers.setAuthorization("TEST AUTH");
        headers.setContentType(MediaType.APPLICATION_JSON);

        task.setHeaders(headers);
        task.setMethod(HttpMethod.POST);
        task.setUrl(new URL("http://test.com"));

        key = "test-job-key";
    }

    @Test
    public void testCreateAndGetTask() throws IOException {

        repository.addTask(task, key);
        Task resultTask = repository.getTaskForJob(key);

        assertEquals(task, resultTask);
    }

    @Test(expected = DataAccessException.class)
    public void testGetThirdPartyTest() {

        repository.getTaskForJob("wrong-key");
    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddWithDuplicationKey() throws IOException {

        repository.addTask(task, key);
        repository.addTask(task, key);
    }

    @Test(expected = TaskException.class)
    public void testAddNull() {
        repository.addTask(null, null);
    }

    @Test(expected = TaskException.class)
    public void testDeleteNull() {
        repository.deleteTaskForJob(null);
    }

    @Test(expected = TaskException.class)
    public void testGetNull() {
        repository.getTaskForJob(null);
    }


    @After
    public void cleanUp() {
        repository.deleteTaskForJob(key);
    }
}
