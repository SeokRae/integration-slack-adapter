
package org.example.slack.core.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.example.slack.core.interceptors.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpClientConfiguration {

  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
      .connectTimeout(5, TimeUnit.SECONDS) // 연결 타임아웃
      .readTimeout(15, TimeUnit.SECONDS)    // 읽기 타임아웃
      .writeTimeout(30, TimeUnit.SECONDS)   // 쓰기 타임아웃
      .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES)) // 연결 풀 설정
      .addInterceptor(new LoggingInterceptor()) // 로깅 인터셉터
      .build();
  }
}