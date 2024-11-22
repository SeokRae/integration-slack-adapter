package org.example.slack.application.messages.adapter;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.slack.application.messages.adapter.exception.SlackMessageException;
import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.common.client.OkHttpClientTemplate;
import org.example.slack.common.exception.HttpClientException;
import org.example.slack.core.props.SlackProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessagesAdapter implements MessageSender {

  private final OkHttpClientTemplate okHttpClientTemplate;
  private final SlackProperties slackProperties;

  @Override
  public SlackMessageResponse sendMessage(SlackPostMessageRequest request) {
    try {

      validateRequest(request);

      Map<String, String> headers = createHeaders();
      SlackMessageResponse response = okHttpClientTemplate.post(
        slackProperties.getPostMessageUrl(),
        headers,
        request,
        SlackMessageResponse.class
      );

      if (!response.isOk()) {
        log.error("Slack API returned error: {}", response.getError());
        throw new SlackMessageException(
          SlackMessageException.ErrorCode.API_ERROR,
          String.format("%s", response.getError())
        );
      }

      log.info("Message sent successfully: channel={}, ts={}",
        request.getChannel(), response.getTs());

      return response;
    } catch (IllegalArgumentException e) {
      log.error("Validation failed for Slack message request: {}", e.getMessage(), e);
      throw new SlackMessageException(SlackMessageException.ErrorCode.INVALID_REQUEST, "Invalid request parameters", e.getMessage());
    } catch (HttpClientException e) {
      log.error("HTTP communication failed: {}", e.getMessage(), e);
      throw new SlackMessageException(SlackMessageException.ErrorCode.NETWORK_ERROR, "Failed to communicate with Slack API", e.getMessage());
    } catch (Exception e) {
      log.error("Unexpected error while sending Slack message: {}", e.getMessage(), e);
      e.getStackTrace();
      throw e;
    }
  }

  private void validateRequest(SlackPostMessageRequest request) {
    if (request == null) {
      log.error("Slack message request is null");
      throw new IllegalArgumentException("Request cannot be null");
    }
    if (StringUtils.isBlank(request.getChannel())) {
      log.error("Slack message request channel is empty");
      throw new IllegalArgumentException("Channel cannot be empty");
    }
    if (StringUtils.isBlank(request.getText()) && (request.getBlocks() == null || request.getBlocks().isEmpty())) {
      log.error("Slack message request must have either text or blocks");
      throw new IllegalArgumentException("Either text or blocks must be provided");
    }
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
