package org.example.slack.core.props;


import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@Slf4j
@Getter
@ToString
@ConfigurationProperties(prefix = "slack")
public class UserProperties {

  private final List<User> users;

  @ConstructorBinding
  public UserProperties(List<User> users) {
    this.users = users;
  }

  @Getter
  @ToString
  public static class User {
    private final String email; // 사용자 이메일
    private final String id;    // 사용자 ID
    private final String name;  // 사용자 이름

    public User(String email, String id, String name) {
      this.email = email;
      this.id = id;
      this.name = name;
      log.info("{}", this);
    }
  }
}
