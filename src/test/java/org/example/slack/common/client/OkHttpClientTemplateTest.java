package org.example.slack.common.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings(value = "NonAsciiCharacters")
@SpringBootTest
class OkHttpClientTemplateTest {

  @Autowired
  private OkHttpClientTemplate okHttpClientTemplate;

  @Test
  void 메시지_전송_테스트() {
    String url = "https://httpbin.org/anything";
    Map<String, String> headers = Map.of(
      "Authorization", "Bearer " + "token",
      "Content-Type", "application/json" // Slack API는 JSON 데이터를 요구함
    );
    String body = "{\"key\":\"value\"}";
    String post = okHttpClientTemplate.post(url, headers, body, String.class);

    assertThat(post).isNotBlank();
  }
}