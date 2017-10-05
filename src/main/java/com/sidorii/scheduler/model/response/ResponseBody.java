package com.sidorii.scheduler.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

//    public HttpStatus getStatusCode() {
//        return HttpStatus.valueOf(code);
//    }
}
