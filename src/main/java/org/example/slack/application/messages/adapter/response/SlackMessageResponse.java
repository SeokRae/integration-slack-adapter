package org.example.slack.application.messages.adapter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.slack.application.model.message.Attachment;
import org.example.slack.application.model.message.Block;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageResponse {
  private boolean ok;                 // 요청 성공 여부
  private String error;               // 오류 메시지 (ok가 false일 때 제공)
  private String warning;             // 경고 메시지
  private String ts;                  // 메시지 타임스탬프
  private String channel;             // 메시지를 보낸 채널 ID
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Message message;            // 메시지 정보

  @ToString
  @EqualsAndHashCode
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Message {
    private String type;            // 메시지 타입 (e.g., "message")
    private String subtype;         // 메시지 하위 타입
    private String text;            // 메시지 텍스트
    private String ts;              // 메시지 타임스탬프
    private String username;        // 메시지 작성자 이름
    private String botId;           // 봇 ID (봇이 작성한 경우)
    private String team;            // 팀 ID
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Attachment> attachments; // 첨부 리스트
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Block> blocks;     // 블록 리스트
  }
}