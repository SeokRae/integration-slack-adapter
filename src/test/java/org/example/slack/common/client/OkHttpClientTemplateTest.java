package org.example.slack.common.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings(value = "NonAsciiCharacters")
@SpringBootTest
class OkHttpClientTemplateTest {

  @Autowired
  private OkHttpClientTemplate okHttpClientTemplate;

  private static Stream<Arguments> providerMockingSite() {
    return Stream.of(
      Arguments.of(
        "https://httpbin.org/anything",
        Map.of(
          "Authorization", "Bearer " + "token",
          "Content-Type", "application/json"
        ),
        "{\"key\":\"value\"}"
      ));
  }

  @DisplayName("OkHttpClient를 사용한 POST 요청 더미 테스트")
  @MethodSource(value = "providerMockingSite")
  @ParameterizedTest(name = "{index} => url={0}, headers={1}, body={2}")
  void 메시지_전송_테스트(
    String url,
    Map<String, String> headers,
    String body
  ) {
    String post = okHttpClientTemplate.post(url, headers, body, String.class);

    assertThat(post).isNotBlank();
  }
}