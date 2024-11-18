package org.example.slack.application.messages.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 JSON에 포함하지 않음
public class SlackPostMessageRequest {

  private String channel;      // 메시지를 보낼 채널 ID 또는 이름 (필수)
  private String text;         // 메시지 내용 (필수)
  private List<Block> blocks;  // 블록 레이아웃 구성 요소 (선택)
  private List<Attachment> attachments; // 첨부 파일 (선택)
  private String threadTs;     // 스레드의 타임스탬프 (선택)
  private Boolean mrkdwn;      // 마크다운 사용 여부 (선택)
  private Boolean replyBroadcast; // 스레드 브로드캐스트 여부 (선택)
  private String iconUrl;      // 사용자 아이콘으로 표시할 이미지 URL (선택)
  private String iconEmoji;    // 사용자 아이콘으로 표시할 이모지 (선택)
  private String username;     // 메시지를 보낼 사용자 이름 (선택)
  private String parse;        // 메시지 텍스트에서 링크 처리 방법 (선택)

  /**
   * 블록 구성 요소 정의
   */
  @Getter
  @Builder
  @ToString
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Block {
    private String type;     // 블록 타입 (예: "section", "divider")
    private Text text;       // 텍스트 구성 요소 (선택)

    @Getter
    @Builder
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Text {
      private String type; // 텍스트 타입 ("mrkdwn" 또는 "plain_text")
      private String text; // 텍스트 내용
    }
  }

  /**
   * 첨부 파일 정의
   */
  @Getter
  @Builder
  @ToString
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Attachment {
    private String fallback; // 지원되지 않는 환경에서 표시할 텍스트
    private String color;    // 첨부 파일의 색상 (예: "#36a64f")
    private String pretext;  // 첨부 파일 위에 표시되는 텍스트
    private String title;    // 첨부 파일 제목
    private String text;     // 첨부 파일 본문 내용
    private List<Field> fields; // 필드 배열 (선택)

    /**
     * 필드 구성 요소 정의
     */
    @Getter
    @Builder
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Field {
      private String title;  // 필드 제목
      private String value;  // 필드 값
      private Boolean isShort; // 필드가 짧게 표시될지 여부
    }
  }
}