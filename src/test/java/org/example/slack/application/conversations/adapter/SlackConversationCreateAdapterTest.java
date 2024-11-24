package org.example.slack.application.conversations.adapter;

import org.example.slack.application.conversations.adapter.create.SlackConversationCreateAdapter;
import org.example.slack.application.conversations.adapter.create.request.SlackConversationCreateRequest;
import org.example.slack.application.conversations.adapter.create.response.SlackConversationCreateResponse;
import org.example.slack.common.factory.SlackChannelNameFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackConversationCreateAdapterTest {

  @Autowired
  private SlackConversationCreateAdapter slackConversationCreateAdapter;

  @DisplayName(value = "슬랙 신규 채널 생성 테스트")
  @Test
  void 채널_생성_테스트() {
    // given
    SlackConversationCreateRequest request = SlackConversationCreateRequest.builder()
      .name(SlackChannelNameFactory.createChannelName("payment", "deploytest"))
      .isPrivate(false)
      .build();

    // when
    SlackConversationCreateResponse slackConversationCreateResponse = slackConversationCreateAdapter.conversationCreate(request);

    // then
    assertThat(slackConversationCreateResponse)
      .isNotNull()
      .satisfies(response -> {
        assertThat(response.isOk()).isTrue();
        assertThat(response.getChannel()).isNotNull();
        assertThat(response.getChannel().getId()).isNotBlank();
        assertThat(response.getChannel().getName()).isNotBlank();
      });
  }
}