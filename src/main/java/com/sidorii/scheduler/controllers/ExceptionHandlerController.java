package com.sidorii.scheduler.controllers;

import com.sidorii.scheduler.model.exception.ConfigurationException;
import com.sidorii.scheduler.model.exception.JobNotFoundException;
import com.sidorii.scheduler.model.exception.TaskException;
import com.sidorii.scheduler.model.response.ResponseBody;
import com.sidorii.scheduler.util.BodyWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Properties;

@RestControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(TaskException.class)
    public BodyWrapper<ResponseBody> taskExceptionHandler(TaskException e) {
        ResponseBody response = new ResponseBody(HttpStatus.INTERNAL_SERVER_ERROR,
                "Exception: " + e.getMessage());

        return BodyWrapper.wrap(response);
    }


    @ExceptionHandler(ConfigurationException.class)
    public BodyWrapper<ResponseBody> configurationExceptionHandler(ConfigurationException e) {
        ResponseBody response = new ResponseBody(HttpStatus.BAD_REQUEST, "Exception: " + e.getMessage());

        return BodyWrapper.wrap(response);
    }


    @ExceptionHandler(JobNotFoundException.class)
    public BodyWrapper<ResponseBody> jobNotFoundExceptionHandler(JobNotFoundException e) {
        ResponseBody response = new ResponseBody(HttpStatus.NOT_FOUND, "Exception: " + e.getMessage());

        return BodyWrapper.wrap(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BodyWrapper<Properties> handleValidationException(MethodArgumentNotValidException exception) {

        List<ObjectError> list = exception.getBindingResult().getAllErrors();

        Properties properties = new Properties();

        for (ObjectError e : list) {
            properties.setProperty(e.getCode(), e.getDefaultMessage());
        }
        return BodyWrapper.wrap(properties);
    }
}
