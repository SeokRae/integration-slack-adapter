package org.example.slack.application.users.adapter;

import org.example.slack.application.users.adapter.request.SlackLookUpByEmailRequest;
import org.example.slack.application.users.adapter.response.SlackLookUpByEmailResponse;
import org.example.slack.core.props.UserProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName(value = "Slack 사용자 조회 테스트")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackUsersAdapterTest {

  @Autowired
  private SlackUsersAdapter slackUsersAdapter;

  @Autowired
  private UserProperties userProperties;

  @DisplayName(value = "이메일 기반으로 사용자 아이디 조회 테스트")
  @Test
  void lookup_user_test() {
    // given
    SlackLookUpByEmailRequest request = SlackLookUpByEmailRequest.builder()
      .email(userProperties.getUsers().get(0).getEmail())
      .build();

    // when
    SlackLookUpByEmailResponse slackLookUpByEmailResponse = slackUsersAdapter.lookUpByEmail(request);

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
}