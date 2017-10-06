package com.sidorii.scheduler.model.task;

import com.sidorii.scheduler.model.CustomHttpHeaders;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.net.URL;

public class HttpTask implements Task {

    @NotNull(message = "{task.method}")
    private HttpMethod method;
    @NotNull(message = "{task.url}")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpTask)) return false;

        HttpTask httpTask = (HttpTask) o;

        if (getMethod() != httpTask.getMethod()) return false;
        if (getUrl() != null ? !getUrl().equals(httpTask.getUrl()) : httpTask.getUrl() != null) return false;
        if (getHeaders() != null ? !getHeaders().equals(httpTask.getHeaders()) : httpTask.getHeaders() != null)
            return false;
        return getData() != null ? getData().equals(httpTask.getData()) : httpTask.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getMethod() != null ? getMethod().hashCode() : 0;
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (getHeaders() != null ? getHeaders().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }
}
