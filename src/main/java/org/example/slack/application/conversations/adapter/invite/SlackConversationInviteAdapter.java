package org.example.slack.application.conversations.adapter.invite;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.slack.application.conversations.adapter.invite.request.SlackConversationInviteRequest;
import org.example.slack.application.conversations.adapter.invite.response.SlackConversationInviteResponse;
import org.example.slack.application.messages.adapter.exception.SlackMessageException;
import org.example.slack.common.client.OkHttpClientTemplate;
import org.example.slack.core.props.SlackProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackConversationInviteAdapter {

  private final OkHttpClientTemplate okHttpClientTemplate;
  private final SlackProperties slackProperties;

  public SlackConversationInviteResponse inviteConversation(SlackConversationInviteRequest request) {

    Map<String, String> headers = createHeaders();

    return okHttpClientTemplate.post(
      "https://slack.com/api/conversations.invite",
      headers,
      request,
      SlackConversationInviteResponse.class
    );
  }

  private Map<String, String> createHeaders() {
    String token = slackProperties.getInformations().getToken();
    if (StringUtils.isBlank(token)) {
      log.error("Slack API token is not configured");
      throw new SlackMessageException(SlackMessageException.ErrorCode.AUTHENTICATION_ERROR, "Slack API token is not configured");
    }

    return Map.of(
      HttpHeaders.AUTHORIZATION, "Bearer " + token,
      HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE
    );
  }
}
