package org.example.slack.application.messages.adapter;

import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.core.props.SlackProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <a href="https://slack.com/intl/ko-kr/help/articles/205240127-Slack%EC%97%90%EC%84%9C-%EB%A9%98%EC%85%98-%EC%82%AC%EC%9A%A9?utm_source=chatgpt.com">...</a>
 */
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackMessagesAdapterMentionTest {

  @Autowired
  private SlackMessagesAdapter slackMessagesAdapter;

  @Autowired
  private SlackProperties slackProperties;

  @Test
  void mention_send_message_테스트() {
    // given
    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(slackProperties.getGeneralChannelName())
      .text("Hello, <@SeokRae Kim>!")
      .build();

    // when
    SlackMessageResponse slackMessageResponse = slackMessagesAdapter.sendMessage(postMessageRequest);

    // then
    assertThat(slackMessageResponse)
      .isNotNull()
      .satisfies(response -> {
        assertThat(response.isOk()).isTrue();
        assertThat(response.getTs()).isNotBlank();
      });

  }
}