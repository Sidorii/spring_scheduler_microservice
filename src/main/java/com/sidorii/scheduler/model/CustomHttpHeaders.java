package com.sidorii.scheduler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sidorii.scheduler.util.HeadersUtil;
import org.springframework.http.HttpHeaders;

import java.util.Collections;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomHttpHeaders extends HttpHeaders {


    public CustomHttpHeaders() {
        super();
        super.setAccept(Collections.singletonList(HeadersUtil.defaultAccept()));
        super.setContentType(HeadersUtil.defaultContentType());
    }

    public void setAuthorization(String authority) {
        add(HttpHeaders.AUTHORIZATION, authority);
    }

}
