package org.example.slack.application.conversations.adapter.invite;

import org.example.slack.application.conversations.adapter.invite.request.SlackConversationInviteRequest;
import org.example.slack.application.conversations.adapter.invite.response.SlackConversationInviteResponse;
import org.example.slack.application.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackConversationInviteAdapterTest {

  @Autowired
  private SlackConversationInviteAdapter slackConversationInviteAdapter;

  private static Stream<Arguments> conversationInvite() {
    return Stream.of(
      Arguments.of(
        "C081ZCTHV6H",
        List.of(
          User.builder().id("U0818G13GA0").build())
      )
    );
  }

  @DisplayName(value = "채널 사용자 초대 테스트")
  @MethodSource(value = "conversationInvite")
  @ParameterizedTest(name = "{index} {displayName} - {arguments}")
  void 사용자_초대_테스트(
    String channel,
    List<User> users
  ) {

    // given
    SlackConversationInviteRequest inviteConversation = SlackConversationInviteRequest.from(channel, users);
    // when
    SlackConversationInviteResponse slackConversationInviteResponse = slackConversationInviteAdapter.inviteConversation(inviteConversation);
    // then
    assertThat(slackConversationInviteResponse)
      .isNotNull()
      .satisfies(response -> {
        assertThat(response.isOk()).isTrue();
        assertThat(response.getChannel()).isNotNull();
        assertThat(response.getChannel().getId()).isNotBlank();
        assertThat(response.getChannel().getName()).isNotBlank();
      });
  }
}