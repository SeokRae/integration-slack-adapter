package org.example.slack.common.serialize;

public interface Serializer {
    <T> String serialize(T object);
    <T> T deserialize(String content, Class<T> clazz);
}
