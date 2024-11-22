package org.example.slack.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.slack.common.exception.HttpClientException;
import org.example.slack.common.exception.HttpClientExceptionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OkHttpClientTemplate {

  private final OkHttpClient client;
  private final ObjectMapper objectMapper;

  public <T> T get(String url, Map<String, String> headers, Map<String, String> queryParams, Class<T> clazz) {
    log.debug("Executing GET request. URL: {}, Headers: {}, Query Params: {}", url, headers, queryParams);
    return executeRequest(buildRequest(url, headers, queryParams, "GET", null), clazz);
  }

  public <T, R> T post(String url, Map<String, String> headers, R body, Class<T> clazz) {
    log.debug("Executing POST request. URL: {}, Headers: {}, Body: {}", url, headers, body);
    return executeRequest(buildRequest(url, headers, null, "POST", body), clazz);
  }

  private <B> Request buildRequest(String url, Map<String, String> headers, Map<String, String> queryParams, String method, B body) {
    try {
      // URL에 쿼리 파라미터 추가
      String fullUrl = appendQueryParams(url, queryParams);

      // Builder 체이닝으로 객체 생성
      Request request = new CustomRequestBuilder()
        .url(fullUrl)
        .method(method, createRequestBody(method, body))
        .headers(headers)
        .build();

      log.debug("Request built successfully. URL: {}, Method: {}, Headers: {}, Query Params: {}", fullUrl, method, headers, queryParams);
      return request;
    } catch (Exception e) {
      log.error("Error occurred while building request. URL: {}, Method: {}, Headers: {}, Query Params: {}", url, method, headers, queryParams, e);
      throw new HttpClientException("Failed to build HTTP request", 0, null, e);
    }
  }

  private String appendQueryParams(String url, Map<String, String> queryParams) {
    if (queryParams == null || queryParams.isEmpty()) {
      return url;
    }

    StringBuilder fullUrl = new StringBuilder(url);
    if (!url.contains("?")) {
      fullUrl.append("?");
    } else if (!url.endsWith("&") && !url.endsWith("?")) {
      fullUrl.append("&");
    }

    queryParams.forEach((key, value) -> {
      fullUrl.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
        .append("=")
        .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
        .append("&");
    });

    // Remove trailing '&' if present
    if (fullUrl.charAt(fullUrl.length() - 1) == '&') {
      fullUrl.setLength(fullUrl.length() - 1);
    }

    return fullUrl.toString();
  }


  private <B> RequestBody createRequestBody(String method, B body) {
    if ("POST".equalsIgnoreCase(method)) {
      if (body != null) {
        String jsonBody = serializeToJson(body); // JSON 변환
        return RequestBody.create(jsonBody, MediaType.get("application/json"));
      } else {
        return RequestBody.create("", MediaType.get("application/json"));
      }
    }
    return null; // GET이나 다른 메서드는 Body가 없음
  }

  private <T> T executeRequest(Request request, Class<T> clazz) {
    log.debug("Executing HTTP request. URL: {}, Method: {}", request.url(), request.method());
    try (Response response = client.newCall(request).execute()) {
      String responseBody = response.body() != null ? response.body().string() : "";

      if (!response.isSuccessful()) {
        log.error("HTTP Request failed. URL: {}, Status Code: {}, Response Body: {}",
          request.url(), response.code(), responseBody);
        throw HttpClientExceptionFactory.createException(response.code(), responseBody);
      }

      log.info("HTTP Request succeeded. URL: {}, Status Code: {}", request.url(), response.code());

      // 응답 타입이 String.class라면 직렬화 없이 원본 문자열 반환
      if (clazz == String.class) {
        return clazz.cast(responseBody);
      }

      // JSON 역직렬화
      return deserializeResponse(responseBody, clazz);
    } catch (IOException e) {
      log.error("HTTP Request execution failed. URL: {}, Method: {}", request.url(), request.method(), e);
      throw new HttpClientException("Failed to execute HTTP request", 0, null, e);
    }
  }

  private <T> T deserializeResponse(String responseBody, Class<T> clazz) {
    try {
      log.debug("Deserializing response. Target Class: {}, Response Body: {}", clazz, responseBody);
      return objectMapper.readValue(responseBody, clazz);
    } catch (Exception e) {
      log.error("Error occurred while deserializing response. Target Class: {}, Response Body: {}", clazz, responseBody, e);
      throw new HttpClientException("Failed to deserialize response", 0, null, e);
    }
  }

  private <B> String serializeToJson(B body) {
    try {
      log.debug("Serializing request body. Body: {}", body);
      return objectMapper.writeValueAsString(body);
    } catch (Exception e) {
      log.error("Error occurred while serializing request body. Body: {}", body, e);
      throw new HttpClientException("Failed to serialize request body", 0, null, e);
    }
  }
}