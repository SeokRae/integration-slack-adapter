package org.example.slack.application.notification;

import org.example.slack.application.conversations.adapter.create.SlackConversationCreateAdapter;
import org.example.slack.application.conversations.adapter.create.request.SlackConversationCreateRequest;
import org.example.slack.application.conversations.adapter.create.response.SlackConversationCreateResponse;
import org.example.slack.application.conversations.adapter.invite.SlackConversationInviteAdapter;
import org.example.slack.application.conversations.adapter.invite.request.SlackConversationInviteRequest;
import org.example.slack.application.conversations.adapter.invite.response.SlackConversationInviteResponse;
import org.example.slack.application.messages.adapter.SlackMessagesAdapter;
import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.application.model.user.User;
import org.example.slack.application.users.adapter.SlackUsersAdapter;
import org.example.slack.application.users.adapter.response.SlackLookUpByEmailResponse;
import org.example.slack.common.factory.SlackChannelNameFactory;
import org.example.slack.core.props.UserProperties;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class SlackNotificationServiceTest {

  @Autowired
  private SlackConversationCreateAdapter slackConversationCreateAdapter;

  @Autowired
  private SlackUsersAdapter slackUsersAdapter;

  @Autowired
  private UserProperties userProperties;

  @Autowired
  private SlackConversationInviteAdapter slackConversationInviteAdapter;
  @Autowired
  private SlackMessagesAdapter slackMessagesAdapter;

  private static List<User> users;
  private static String channel;

  @Order(1)
  @DisplayName(value = "슬랙 신규 채널 생성 테스트")
  @Test
  void 채널_생성_테스트() {
    // given
    SlackConversationCreateRequest request = SlackConversationCreateRequest.builder()
      .name(SlackChannelNameFactory.createChannelName("payments-notification", "deploy-integration-test"))
      .isPrivate(false)
      .build();

    // when
    SlackConversationCreateResponse slackConversationCreateResponse = slackConversationCreateAdapter.conversationCreate(request);

    channel = slackConversationCreateResponse.getChannel().getId();
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

  @Order(2)
  @DisplayName(value = "이메일 기반으로 사용자 아이디 조회 테스트")
  @Test
  void lookup_user_test() {
    // given
    String email = userProperties.getUsers().get(0).getEmail();

    // when
    SlackLookUpByEmailResponse slackLookUpByEmailResponse = slackUsersAdapter.lookupUserIdByEmail(email);

    users = List.of(slackLookUpByEmailResponse.getUser());
    // then
    assertThat(slackLookUpByEmailResponse)
      .isNotNull()
      .satisfies(response -> {
        assertThat(response.isOk()).isTrue();
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getId()).isNotBlank();
        assertThat(response.getUser().getName()).isNotBlank();
        assertThat(response.getUser().getProfile()).isNotNull();
        assertThat(response.getUser().getProfile().getEmail()).isNotBlank();
      });
  }

  @Order(3)
  @DisplayName(value = "채널 사용자 초대 테스트")
  @Test
  void 사용자_초대_테스트() {
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

  @Order(4)
  @Test
  @DisplayName("Slack 메시지를 전송하고 스레드에 답장을 보낼 수 있다")
  void sendMessageAndReplyToThread() {
    // given
    String message = "테스트 알람 메시지입니다.";

    SlackPostMessageRequest originalRequest = SlackPostMessageRequest.builder()
      .channel(channel)
      .text(message)
      .build();

    // when
    SlackMessageResponse originalResponse = slackMessagesAdapter.sendMessage(originalRequest);

    // then
    assertThat(originalResponse).isNotNull();
    assertThat(originalResponse.isOk()).isTrue();
    assertThat(originalResponse.getTs()).isNotEmpty();

    // given - thread reply
    String threadMessage = "스레드 답장 메시지입니다.";
    SlackPostMessageRequest threadRequest = SlackPostMessageRequest.builder()
      .channel(channel)
      .text(threadMessage)
      .threadTs(originalResponse.getTs()) // 원본 메시지의 ts를 threadTs로 설정
      .build();

    // when - send thread reply
    SlackMessageResponse threadResponse = slackMessagesAdapter.sendMessage(threadRequest);

    // then - verify thread reply
    assertThat(threadResponse).isNotNull();
    assertThat(threadResponse.isOk()).isTrue();
    assertThat(threadResponse.getTs())
      .isNotEmpty()
      .isEqualTo(originalResponse.getTs());
  }
}