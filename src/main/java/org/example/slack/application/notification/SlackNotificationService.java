package org.example.slack.application.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.slack.application.conversations.adapter.create.SlackConversationCreateAdapter;
import org.example.slack.application.conversations.adapter.create.request.SlackConversationCreateRequest;
import org.example.slack.application.conversations.adapter.create.response.SlackConversationCreateResponse;
import org.example.slack.application.conversations.adapter.invite.SlackConversationInviteAdapter;
import org.example.slack.application.conversations.adapter.invite.request.SlackConversationInviteRequest;
import org.example.slack.application.conversations.adapter.invite.response.SlackConversationInviteResponse;
import org.example.slack.application.files.adapter.SlackFilesAdapter;
import org.example.slack.application.files.adapter.request.SlackFileUploadRequest;
import org.example.slack.application.files.adapter.response.SlackFileUploadResponse;
import org.example.slack.application.messages.adapter.SlackMessagesAdapter;
import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.application.model.user.User;
import org.example.slack.application.users.adapter.SlackUsersAdapter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackNotificationService {

  private final SlackConversationCreateAdapter conversationCreate; // 채널 생성, 사용자 초대 담당
  private final SlackUsersAdapter userAdapter;                 // 사용자 조회 담당
  private final SlackConversationInviteAdapter conversationInvite; // 채널 생성, 사용자 초대 담당
  private final SlackMessagesAdapter messageAdapter;           // 메시지 전송 담당
  private final SlackFilesAdapter fileAdapter;                 // 파일 업로드 담당

  /**
   * Slack 알림 시나리오 실행
   *
   * @param channelName 채널 이름
   * @param emails      초대할 사용자 이메일 목록
   * @param messageText 채널에 전송할 메시지
   * @param filePath    업로드할 파일 경로
   */
  public void executeSlackScenario(String channelName, List<String> emails, String messageText, String filePath) {
    // 1. 채널 생성
    String channelId = createChannel(channelName);

    // 2. 사용자 초대
    inviteUsersToChannel(channelId, emails);

    // 3. 메시지 전송
    String threadTs = sendMessage(channelId, messageText);

    // 4. 파일 업로드
    uploadFileToThread(channelId, filePath, threadTs);
  }

  private String createChannel(String channelName) {

    SlackConversationCreateRequest conversationCreateRequest = SlackConversationCreateRequest.builder()
      .name(channelName)
      .build();
    SlackConversationCreateResponse response = conversationCreate.conversationCreate(conversationCreateRequest);
    if (!response.isOk()) {
      throw new RuntimeException("Failed to create Slack channel: " + response.getError());
    }
    return response.getChannel().getId();
  }

  private void inviteUsersToChannel(String channelId, List<String> emails) {
    List<User> userList = new ArrayList<>();
    for (String email : emails) {
      User user = userAdapter.lookupUserIdByEmail(email).getUser();
      userList.add(user);
    }
    SlackConversationInviteRequest build = SlackConversationInviteRequest.from(channelId, userList);
    SlackConversationInviteResponse slackConversationInviteResponse = conversationInvite.inviteConversation(build);

  }

  //
  private String sendMessage(String channelId, String messageText) {

    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(channelId)
      .text(messageText)
      .build();

    SlackMessageResponse slackMessageResponse = messageAdapter.sendMessage(postMessageRequest);
    if (!slackMessageResponse.isOk()) {
      throw new RuntimeException("Failed to send Slack message: " + slackMessageResponse.getError());
    }
    return slackMessageResponse.getTs(); // Thread Timestamp 반환
  }

  private void uploadFileToThread(String channelId, String filePath, String threadTs) {
    SlackFileUploadRequest slackFileUploadRequest = SlackFileUploadRequest.builder()
      .channelId(channelId)
      .fileName(filePath)
      .build();
    SlackFileUploadResponse slackFileUploadResponse = fileAdapter.uploadFile(slackFileUploadRequest);
    if (!slackFileUploadResponse.isOk()) {
      throw new RuntimeException("Failed to upload file to Slack: " + slackFileUploadResponse.getError());
    }
  }
}
