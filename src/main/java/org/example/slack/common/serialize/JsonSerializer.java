package org.example.slack.common.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonSerializer implements Serializer {
    private final ObjectMapper objectMapper;

    public JsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> String serialize(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize object", e);
        }
    }

    @Override
    public <T> T deserialize(String content, Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize content", e);
        }
    }
}