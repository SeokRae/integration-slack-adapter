package org.example.slack.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.slack.common.exception.HttpClientException;
import org.example.slack.common.exception.HttpClientExceptionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OkHttpClientTemplate {

  private final OkHttpClient client;
  private final ObjectMapper objectMapper;

  public <T> T get(String url, Map<String, String> headers, Class<T> clazz) {
    return executeRequest(buildRequest(url, headers, "GET", null), clazz);
  }

  public <T> T post(String url, Map<String, String> headers, String body, Class<T> clazz) {
    return executeRequest(buildRequest(url, headers, "POST", body), clazz);
  }

  private Request buildRequest(String url, Map<String, String> headers, String method, String body) {
    Request.Builder builder = new Request.Builder().url(url);

    // HTTP 메서드 설정
    switch (method) {
      case "GET" -> builder.get();
      case "POST" -> builder.post(RequestBody.create(body, MediaType.get("application/json")));
      default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
    }

    // 헤더 추가
    headers.forEach(builder::addHeader);

    return builder.build();
  }

  private <T> T executeRequest(Request request, Class<T> clazz) {
    try (Response response = client.newCall(request).execute()) {
      String responseBody = response.body() != null ? response.body().string() : "";

      if (!response.isSuccessful()) {
        log.error("HTTP Request failed. URL: {}, Status Code: {}, Response Body: {}",
          request.url(), response.code(), responseBody);
        throw HttpClientExceptionFactory.createException(response.code(), responseBody);
      }

      log.info("HTTP Request succeeded. URL: {}, Status Code: {}", request.url(), response.code());

      // 요청 타입이 String.class인 경우, JSON 역직렬화 없이 원본 문자열 반환
      if (clazz == String.class) {
        return clazz.cast(responseBody);
      }

      // JSON 역직렬화
      return objectMapper.readValue(responseBody, clazz);
    } catch (IOException e) {
      log.error("HTTP Request execution failed. URL: {}", request.url(), e);
      throw new HttpClientException("Failed to execute HTTP request", 0, null, e);
    }
  }

  private boolean isJsonResponse(String contentType) {
    return contentType != null && contentType.toLowerCase().contains("application/json");
  }
}
