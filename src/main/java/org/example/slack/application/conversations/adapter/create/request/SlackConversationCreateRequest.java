package org.example.slack.application.conversations.adapter.create.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SlackConversationCreateRequest {
  private String name; // 채널 이름
  private boolean isPrivate = false; // 비공개 채널 여부, 기본값 false
}
