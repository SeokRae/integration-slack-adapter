package org.example.slack.application.messages.adapter.exception;

import lombok.Getter;

@Getter
public class SlackMessageException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String details;

  // 주 생성자
  public SlackMessageException(ErrorCode errorCode, String message, Throwable cause, String details) {
    // super에 기본 메시지와 추가 메시지를 조합해 전달
    super(String.format("[%s] %s%s",
      errorCode.getCode(),
      message,
      details != null ? ": " + details : ""));
    this.errorCode = errorCode;
    this.details = details;
  }

  // 단일 매개변수 생성자
  public SlackMessageException(String message) {
    this(ErrorCode.API_ERROR, message, null, null);
  }

  // 메시지와 원인 예외를 받는 생성자
  public SlackMessageException(String message, Throwable cause) {
    this(ErrorCode.API_ERROR, message, cause, null);
  }

  // 에러 코드와 세부 정보를 받는 생성자
  public SlackMessageException(ErrorCode errorCode, String details) {
    this(errorCode, errorCode.getDefaultMessage(), null, details);
  }

  // 에러 코드와 메시지, 세부 정보를 받는 생성자
  public SlackMessageException(ErrorCode errorCode, String message, String details) {
    this(errorCode, message, null, details);
  }

  @Override
  public String getMessage() {
    // super.getMessage()에는 이미 완전한 메시지가 포함되어 있음
    return super.getMessage();
  }

  @Getter
  public enum ErrorCode {
    INVALID_REQUEST("SLK-001", "Invalid request parameters"),
    AUTHENTICATION_ERROR("SLK-002", "Authentication failed"),
    API_ERROR("SLK-003", "Slack API error"),
    NETWORK_ERROR("SLK-004", "Network communication failed"),
    INTERNAL_ERROR("SLK-005", "Internal server error");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
      this.code = code;
      this.defaultMessage = defaultMessage;
    }
  }
}