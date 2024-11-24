package org.example.slack.application.model.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
  private String id; // 생성된 채널 ID
  private String name; // 채널 이름
}
