package com.sidorii.scheduler.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sidorii.scheduler.model.exception.TaskNotFoundException;
import com.sidorii.scheduler.model.task.Task;

import java.io.IOException;

public class SimpleDeserializer extends JsonDeserializer<Task> {
    private static ObjectMapper mapper = new ObjectMapper();


    @Override
    public Task deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        try {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode nodes = oc.readTree(jsonParser);

            JsonNode contextNodes = context.getParser().getCodec().readTree(context.getParser());
            String type = contextNodes.get("type").asText();

            return mapper.readValue(nodes.toString(), TaskFactory.findTaskByType(type).getClass());
        } catch (TaskNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
}
