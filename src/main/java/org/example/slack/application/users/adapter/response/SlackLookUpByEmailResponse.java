package org.example.slack.application.users.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.slack.application.model.user.User;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SlackLookUpByEmailResponse {
  private boolean ok; // 요청 성공 여부
  private User user;  // 사용자 정보
}
