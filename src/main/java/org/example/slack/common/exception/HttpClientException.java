package org.example.slack.common.exception;

public class HttpClientException extends RuntimeException {
  private final int statusCode;
  private final String responseBody;

  public HttpClientException(String message, int statusCode, String responseBody) {
    super(message);
    this.statusCode = statusCode;
    this.responseBody = responseBody;
  }

  public HttpClientException(String message, int statusCode, String responseBody, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
    this.responseBody = responseBody;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getResponseBody() {
    return responseBody;
  }
}
