package org.example.slack.application.conversations.adapter.create.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.slack.application.model.conversation.Channel;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SlackConversationCreateResponse {
  private boolean ok; // 요청 성공 여부
  private Channel channel;
  private String error; // 에러 메시지 (요청 실패 시)
}
