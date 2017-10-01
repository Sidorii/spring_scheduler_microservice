package com.sidorii.scheduler.model.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        super();
    }

    public TaskNotFoundException(String e) {
        super(e);
    }
}
