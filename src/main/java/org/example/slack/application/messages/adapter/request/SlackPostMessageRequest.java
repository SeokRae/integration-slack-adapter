package org.example.slack.application.messages.adapter.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.slack.application.model.message.Attachment;
import org.example.slack.application.model.message.Block;

import java.util.List;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SlackPostMessageRequest {

  @NotBlank(message = "channel is required")
  private String channel;
  private String text;
  private String threadTs;
  private Boolean mrkdwn;
  private List<Attachment> attachments;
  private List<Block> blocks;
  private Boolean replyBroadcast;
  private Boolean unfurlLinks;
  private Boolean unfurlMedia;
  private Boolean asUser;
  private String iconEmoji;
  private String iconUrl;
  private String username;

  // 정적 팩토리 메서드
  public static SlackPostMessageRequest of(String channel, String text) {
    return SlackPostMessageRequest.builder()
      .channel(channel)
      .text(text)
      .build();
  }

  public static SlackPostMessageRequest ofBlocks(String channel, List<Block> blocks) {
    return SlackPostMessageRequest.builder()
      .channel(channel)
      .blocks(blocks)
      .build();
  }

  // 검증 메서드
  public void validate() {
    if (text == null && (blocks == null || blocks.isEmpty())) {
      throw new IllegalArgumentException("Either text or blocks must be provided");
    }
  }
}