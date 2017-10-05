package com.sidorii.scheduler.model.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException() {
    }

    public JobNotFoundException(String message) {
        super(message);
    }
}
