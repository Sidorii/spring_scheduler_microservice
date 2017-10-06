package com.sidorii.scheduler.model.response;

import org.springframework.http.HttpStatus;

public class ResponseBody {

    private Integer code;
    private String body;

    public ResponseBody(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public ResponseBody(HttpStatus code, String body) {
        this.code = code.value();
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public Integer getCode() {
        return code;
    }

}
