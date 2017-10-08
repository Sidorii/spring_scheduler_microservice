package com.sidorii.scheduler.repository;

import com.sidorii.scheduler.model.exception.TaskException;
import com.sidorii.scheduler.model.task.Task;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@PropertySource("classpath:task_queries.properties")
public class JdbcTaskRepository implements TaskRepository {


    private static Logger LOGGER = LoggerFactory.getLogger(JdbcTaskRepository.class);

    @Value("${create}")
    private String CREATE;
    @Value("${delete}")
    private String DELETE;
    @Value("${select}")
    private String SELECT;

    @Autowired
    private JdbcTemplate template;

    public JdbcTaskRepository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public void addTask(Task task, String key) {

        checkNotNull("addTask()", task, key);

        try {
            ByteArrayInputStream taskInput = TaskSerializer.serializeTask(task);

            template.update(CREATE, key, taskInput);


            LOGGER.info("New task with key [{}] added in BD ", key);
        } catch (IOException e) {
            LOGGER.error("Cannot add new task [{}] to DB: {}", task, e.getMessage());
            throw new TaskException("Failed to add new task to DB because: " + e.getCause());
        }
    }

    @Override
    public void deleteTaskForJob(String key) {
        checkNotNull("deleteTaskForJob()", key);

        template.update(DELETE, key);
    }

    @Override
    public Task getTaskForJob(String key) {
        checkNotNull("getTaskForJob()", key);

        return template.queryForObject(SELECT, new Object[]{key}, TaskMapper.INSTANCE);
    }


    protected static class TaskMapper implements RowMapper<Task> {

        protected static final TaskMapper INSTANCE = new TaskMapper();

        @Override
        public Task mapRow(ResultSet resultSet, int i) throws SQLException {
            try {
                Blob blob = resultSet.getBlob(2);

                return TaskSerializer.deserializeTask(blob);
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.error("Cannot deserialize Task instance from DB");
                throw new TaskException("Cannot deserialize Task instance from DB", e);
            }
        }
    }

    protected static class TaskSerializer {

        private static Task deserializeTask(Blob blobTask) throws SQLException, IOException, ClassNotFoundException {
            int blobLength = (int) blobTask.length();

            byte[] taskBytes = blobTask.getBytes(1, blobLength);
            blobTask.free();

            ObjectInputStream is = new ObjectInputStream(
                    new ByteArrayInputStream(taskBytes));

            return (Task) is.readObject();
        }


        protected static ByteArrayInputStream serializeTask(Task task) throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(output);

            oos.writeObject(task);
            oos.flush();

            return new ByteArrayInputStream(output.toByteArray());
        }
    }

    private static void checkNotNull(String method, Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                throw new TaskException("Failed to execute " + method + " because input is null");
            }
        }
    }
}

