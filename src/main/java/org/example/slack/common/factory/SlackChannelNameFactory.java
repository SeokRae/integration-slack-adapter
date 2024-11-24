package org.example.slack.common.factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SlackChannelNameFactory {
  private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
  private static final String ALLOWED_CHARACTERS = "[^a-z0-9-]";

  private SlackChannelNameFactory() {
    // 인스턴스화 방지
  }

  /**
   * 채널 이름 생성
   *
   * @param serviceName 서비스 이름 (필수)
   * @param keyword     특정 키워드 (선택)
   * @return Slack 규칙에 맞게 변환된 채널 이름
   * @throws IllegalArgumentException 서비스 이름이 비어 있거나 유효하지 않을 경우
   */
  public static String createChannelName(String serviceName, String keyword) {
    validateServiceName(serviceName);

    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    String rawName = String.format("%s-%s-%s", serviceName, timestamp, keyword != null ? keyword : "").trim();

    return formatChannelName(rawName);
  }

  /**
   * 서비스 이름 유효성 검사
   *
   * @param serviceName 서비스 이름
   */
  private static void validateServiceName(String serviceName) {
    if (serviceName == null || serviceName.isBlank()) {
      throw new IllegalArgumentException("Service name cannot be null or blank");
    }
  }

  /**
   * Slack 규칙에 맞게 채널 이름 포맷팅
   *
   * @param rawName 원본 채널 이름
   * @return 변환된 채널 이름
   */
  private static String formatChannelName(String rawName) {
    String formattedName = rawName.toLowerCase(); // 소문자로 변환
    formattedName = formattedName.replaceAll(ALLOWED_CHARACTERS, "-"); // 허용되지 않는 문자 제거
    formattedName = formattedName.replaceAll("-+", "-"); // 연속된 하이픈을 하나로 축소
    formattedName = formattedName.replaceAll("^-|-$", ""); // 시작과 끝의 하이픈 제거

    if (formattedName.isBlank()) {
      throw new IllegalArgumentException("Formatted channel name is invalid");
    }

    return formattedName;
  }
}
