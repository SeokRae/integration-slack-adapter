package org.example.slack.application.users.adapter.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Builder
@ToString
public class SlackLookUpByEmailRequest {
  private String email;

  public Map<String, String> toQueryParams() {
    Map<String, String> queryParams = new HashMap<>();

    addIfNotNull(queryParams, "email", email);

    return queryParams;
  }

  private static void addIfNotNull(Map<String, String> map, String key, Object value) {
    Optional.ofNullable(value).ifPresent(v -> map.put(key, String.valueOf(v)));
  }
}
