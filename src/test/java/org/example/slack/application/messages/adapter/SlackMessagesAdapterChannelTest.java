package org.example.slack.application.messages.adapter;

import org.example.slack.application.messages.adapter.exception.SlackMessageException;
import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.core.props.SlackProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName(value = "Slack ")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackMessagesAdapterChannelTest {

  @Autowired
  private SlackMessagesAdapter slackMessagesAdapter;
  @Autowired
  private SlackProperties slackProperties;

  @DisplayName(value = "Channel 메시지 발송 테스트")
  @Test
  void Channel_Send_Message_테스트() {
    // given
    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(slackProperties.getGeneralChannelName())
      .text("Channel 메시지 발송 테스트")
      .build();

    // when
    SlackMessageResponse slackMessageResponse = slackMessagesAdapter.sendMessage(postMessageRequest);

    // then
    assertThat(slackMessageResponse)
      .isNotNull()
      .satisfies(response -> {
        assertThat(response.isOk()).isTrue();
        assertThat(response.getChannel()).isNotBlank();
        assertThat(response.getTs()).isNotBlank();
      });
  }

  @DisplayName(value = "InValid Channel 메시지 발송 테스트")
  @Test
  void InValid_Channel_Send_Message_테스트() {
    // given
    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel("invalid-channel")
      .text("InValid Channel 메시지 발송 테스트")
      .build();

    // then
    assertThatExceptionOfType(SlackMessageException.class)
      .isThrownBy(() -> slackMessagesAdapter.sendMessage(postMessageRequest))
      .satisfies(e -> {
        assertThat(e.getMessage()).contains("channel_not_found");
      });

  }
}