package org.example.slack.application.files.adapter.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackFileUploadRequest {
  private final String channelId;
  private final String fileName;
  private final byte[] fileContent;
  private final String fileType;

  public static SlackFileUploadRequest of(String channelId, String fileName, byte[] fileContent, String fileType) {
    return SlackFileUploadRequest.builder()
      .channelId(channelId)
      .fileName(fileName)
      .fileContent(fileContent)
      .fileType(fileType)
      .build();
  }
}