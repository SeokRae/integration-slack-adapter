package org.example.slack.common.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRequestBuilder<T> {

  private final ObjectMapper objectMapper;
  private T object;

  private JsonRequestBuilder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public static <T> JsonRequestBuilder<T> builder(Class<T> clazz) {
    return new JsonRequestBuilder<>(new ObjectMapper());
  }

  public JsonRequestBuilder<T> withObject(T object) {
    this.object = object;
    return this;
  }

  public String build() {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize object to JSON", e);
    }
  }
}
