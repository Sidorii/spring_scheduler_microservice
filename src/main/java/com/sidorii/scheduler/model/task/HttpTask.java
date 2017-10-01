package com.sidorii.scheduler.model.task;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sidorii.scheduler.model.CustomHttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.net.URL;


public class HttpTask implements Serializable, Task {

    private HttpMethod method;
    private URL url;

    private CustomHttpHeaders headers;
    private String data;


    public HttpTask() {
        headers = new CustomHttpHeaders();
    }


//    Getters

    public CustomHttpHeaders getHeaders() {
        return headers;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URL getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }

//    Setters

    public void setHeaders(CustomHttpHeaders headers) {
        this.headers = headers;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setData(String data) {
        this.data = data;
    }




    @Override
    public String toString() {
        return "{ method: " + method + " url: " + url + " headers: " + headers + " body: " + data + " }";
    }
}
