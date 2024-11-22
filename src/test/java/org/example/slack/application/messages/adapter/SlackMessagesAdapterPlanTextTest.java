package org.example.slack.application.messages.adapter;

import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.core.props.SlackProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackMessagesAdapterPlanTextTest {

  @Autowired
  private SlackMessagesAdapter slackMessagesAdapter;
  @Autowired
  private SlackProperties slackProperties;

  @DisplayName(value = "PlanText 메시지 발송 테스트")
  @Test
  void PlanText_Send_Message_테스트() {
    // given
    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(slackProperties.getGeneralChannelName())
      .text("PlanText 메시지 발송 테스트")
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

  @DisplayName(value = "Emoji 메시지 발송 테스트")
  @Test
  void Emoji_Send_Message_테스트() {
    // given
    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(slackProperties.getGeneralChannelName())
      .text(":rocket: Emoji 메시지 발송 테스트")
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

  @DisplayName(value = "Markdown 메시지 발송 테스트")
  @Test
  void Markdown_Send_Message_테스트() {
    // when
    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(slackProperties.getGeneralChannelName())
      .text("*Bold text* _italic_ ~strikethrough~ `code`")
      .build();

    // then
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
}