package com.sidorii.scheduler.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize()
public class BodyWrapper<T> {

    private T body;

    @JsonCreator
    public BodyWrapper(@JsonProperty("body") T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public static <U> BodyWrapper<U> wrap(U body) {
        return new BodyWrapper<>(body);
    }
}
