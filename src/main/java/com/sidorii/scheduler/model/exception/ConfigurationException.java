package com.sidorii.scheduler.model.exception;

import org.quartz.SchedulerException;

public class ConfigurationException extends Exception{

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String s){
        super(s);
    }

    public ConfigurationException(String s, SchedulerException e) {
    }
}
