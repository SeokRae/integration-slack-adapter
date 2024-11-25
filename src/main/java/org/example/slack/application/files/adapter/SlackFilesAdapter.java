package org.example.slack.application.files.adapter;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.example.slack.application.files.adapter.request.SlackFileUploadRequest;
import org.example.slack.application.files.adapter.response.SlackFileUploadResponse;
import org.example.slack.application.messages.adapter.exception.SlackMessageException;
import org.example.slack.common.client.OkHttpClientTemplate;
import org.example.slack.core.props.SlackProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackFilesAdapter {
  private final OkHttpClientTemplate okHttpClientTemplate;
  private final SlackProperties slackProperties;

  public SlackFileUploadResponse uploadFile(SlackFileUploadRequest request) {

    Map<String, String> headers = createHeaders();
    MultipartBody.Builder builder = new MultipartBody.Builder()
      .setType(MultipartBody.FORM)
      .addFormDataPart("channels", request.getChannelId())
      .addFormDataPart("filename", request.getFileName())
      .addFormDataPart("file", request.getFileName(),
        RequestBody.create(request.getFileContent(), MediaType.parse(request.getFileType())));


    SlackFileUploadResponse slackFileUploadResponse = okHttpClientTemplate.post(
      slackProperties.getUploadFileUrl(),
      headers,
      builder.build(),
      SlackFileUploadResponse.class
    );

    log.info("File uploaded successfully: {}", request.getFileName());
    return slackFileUploadResponse;
  }


  private Map<String, String> createHeaders() {
    String token = slackProperties.getInformations().getToken();
    if (StringUtils.isBlank(token)) {
      log.error("Slack API token is not configured");
      throw new SlackMessageException(SlackMessageException.ErrorCode.AUTHENTICATION_ERROR, "Slack API token is not configured");
    }

    return Map.of(
      HttpHeaders.AUTHORIZATION, "Bearer " + token,
      HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE
    );
  }
}
