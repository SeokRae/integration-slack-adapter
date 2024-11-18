package org.example.slack.common.exception;

public class HttpClientExceptionFactory {

  /**
   * 상태 코드에 따른 예외 생성
   *
   * @param statusCode   HTTP 상태 코드
   * @param responseBody 응답 본문
   * @return HttpClientException
   */
  public static HttpClientException createException(int statusCode, String responseBody) {
    return switch (statusCode) {
      case 400 -> new HttpClientException("Bad Request", statusCode, responseBody);
      case 401 -> new HttpClientException("Unauthorized", statusCode, responseBody);
      case 403 -> new HttpClientException("Forbidden", statusCode, responseBody);
      case 404 -> new HttpClientException("Not Found", statusCode, responseBody);
      case 500 -> new HttpClientException("Internal Server Error", statusCode, responseBody);
      default -> new HttpClientException("Unexpected Error", statusCode, responseBody);
    };
  }
}
