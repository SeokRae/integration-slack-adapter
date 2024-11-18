package org.example.slack.core.props;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackPropertiesTest {

  @Autowired
  private SlackProperties slackProperties;

  @DisplayName("SlackProperties 프로퍼티 정보 로드 확인 테스트")
  @Test
  void testCase1() {
    assertThat(slackProperties)
      .isNotNull()
      .satisfies(properties -> {
        /* 도메인 정보 확인 */
        assertThat(properties.getDomain()).isNotBlank();
        /* 채널 정보 확인 */
        assertThat(properties.getChannels())
          .isNotNull()
          .satisfies(channels -> {
            assertThat(channels.getGeneral()).isNotBlank();
          });

        /* 토큰 확인 */
        assertThat(properties.getToken()).isNotBlank();

        /* URI 정보 확인 */
        assertThat(properties.getUris())
          .isNotNull()
          .satisfies(uris -> {
            assertThat(uris.getConversations())
              .isNotNull()
              .satisfies(conversations -> {
                assertThat(conversations.getCreate()).isNotBlank();
                assertThat(conversations.getInvite()).isNotBlank();
                assertThat(conversations.getList()).isNotBlank();
              });
          });
      });
  }
}