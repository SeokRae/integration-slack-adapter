package org.example.slack.application.users.adapter;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.slack.application.messages.adapter.exception.SlackMessageException;
import org.example.slack.application.users.adapter.request.SlackLookUpByEmailRequest;
import org.example.slack.application.users.adapter.response.SlackLookUpByEmailResponse;
import org.example.slack.common.client.OkHttpClientTemplate;
import org.example.slack.core.props.SlackProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <a href="https://api.slack.com/methods/users.lookupByEmail">users.lookupByEmail</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SlackUsersAdapter implements UserResolvable {

  private final OkHttpClientTemplate okHttpClientTemplate;
  private final SlackProperties slackProperties;

  @Override
  public SlackLookUpByEmailResponse lookUpByEmail(SlackLookUpByEmailRequest request) {

    Map<String, String> headers = createHeaders();
    return okHttpClientTemplate.get(
      "https://slack.com/api/users.lookupByEmail",
      headers,
      request.toQueryParams(),
      SlackLookUpByEmailResponse.class
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
