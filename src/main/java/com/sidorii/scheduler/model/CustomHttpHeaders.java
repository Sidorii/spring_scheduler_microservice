package com.sidorii.scheduler.model;

import org.springframework.http.HttpHeaders;

public class CustomHttpHeaders extends HttpHeaders {

    public void setAuthorization(String authority) {
        add(HttpHeaders.AUTHORIZATION, authority);
    }
}
