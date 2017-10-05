package com.sidorii.scheduler.model.exception;

public class TaskException extends RuntimeException {

    public TaskException() {
        super();
    }

    public TaskException(String e) {
        super(e);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
