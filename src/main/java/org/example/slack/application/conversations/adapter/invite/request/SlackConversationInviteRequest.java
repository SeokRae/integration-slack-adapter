package org.example.slack.application.conversations.adapter.invite.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.slack.application.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
public class SlackConversationInviteRequest {
  private final String channel; // 채널 ID
  private final String users;   // 초대할 사용자 ID 목록

  /**
   * 생성 시 사용자 ID 목록을 쉼표로 연결
   */
  public static SlackConversationInviteRequest from(String channel, List<User> users) {
    // User 객체에서 ID만 추출
    String userIds = users.stream()
      .map(User::getId) // User 객체에서 ID 추출
      .collect(Collectors.joining(",")); // 쉼표로 구분
    return SlackConversationInviteRequest.builder()
      .channel(channel)
      .users(userIds)
      .build();
  }
}
