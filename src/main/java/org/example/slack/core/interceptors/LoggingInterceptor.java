package org.example.slack.core.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class LoggingInterceptor implements Interceptor {

  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {
    Request request = chain.request();

    // 요청 시간 측정 시작
    long startTime = System.nanoTime();

    try {
      // 요청 정보 로깅
      logRequest(request);

      // 요청 수행 및 응답 받기
      Response response = chain.proceed(request);

      // 요청-응답 시간 측정
      long endTime = System.nanoTime();
      long durationMs = (endTime - startTime) / 1_000_000; // 나노초 -> 밀리초 변환

      // 응답 정보 로깅
      logResponse(response, durationMs);

      return response;
    } catch (Exception e) {
      log.error("HTTP Request failed: {}", request.url(), e);
      throw e;
    }
  }

  private void logRequest(Request request) {
    if (log.isInfoEnabled()) {
      log.info("================================================== Request Begin ==================================================");
      log.info("URL         : {}", request.url());
      log.info("Method      : {}", request.method());
      log.info("Headers     : {}", formatHeaders(request.headers()));

      if (request.body() != null) {
        try {
          Buffer buffer = new Buffer();
          request.body().writeTo(buffer);
          log.info("Request Body: {}", buffer.readUtf8());
        } catch (IOException e) {
          log.warn("Failed to read request body: {}", e.getMessage());
        }
      }

      log.info("================================================== Request End ==================================================");
    }
  }

  private void logResponse(Response response, long durationMs) {
    if (log.isInfoEnabled()) {
      log.info("================================================== Response Begin ==================================================");
      log.info("URL           : {}", response.request().url());
      log.info("Status Code   : {}", response.code());
      log.info("Duration      : {} ms", durationMs);
      log.info("Headers       : {}", formatHeaders(response.headers()));

      if (response.body() != null) {
        try {
          String responseBody = response.peekBody(Long.MAX_VALUE).string();
          log.info("Response Body : {}", responseBody);
        } catch (IOException e) {
          log.warn("Failed to read response body: {}", e.getMessage());
        }
      }

      log.info("================================================== Response End ==================================================");
    }
  }

  private String formatHeaders(Headers headers) {
    StringBuilder formattedHeaders = new StringBuilder();
    for (String name : headers.names()) {
      formattedHeaders.append(name).append(": ").append(headers.get(name)).append("\n");
    }
    return formattedHeaders.toString().trim();
  }
}