package com.sidorii.scheduler.util;

import com.sidorii.scheduler.model.exception.TaskNotFoundException;
import com.sidorii.scheduler.model.task.HttpTask;
import com.sidorii.scheduler.model.task.Task;

public class TaskFactory {

    public static Task findTaskByType(String type) {

        switch (type){
            case "http":
                return new HttpTask();
            default:
                throw new TaskNotFoundException("Not required task for type " + type);
        }
    }
}
