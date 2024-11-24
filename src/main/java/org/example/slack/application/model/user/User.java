package org.example.slack.application.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

/**
 * <a href="https://api.slack.com/types/user">User</a>
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
  private String id; // 사용자 고유 ID
  private String teamId; // 팀 ID
  private String name; // Slack 사용자 이름
  private boolean deleted; // 사용자 비활성화 여부
  private String color; // 사용자 색상 코드
  private String realName; // 실제 이름
  private String tz; // 시간대
  private String tzLabel; // 시간대 레이블
  private int tzOffset; // 시간대 오프셋 (초 단위)
  private Profile profile; // 사용자 프로필 정보
  @JsonProperty("is_admin") // 명시적으로 매핑
  private boolean isAdmin;
  @JsonProperty("is_owner")
  private boolean isOwner;
  @JsonProperty("is_primary_owner")
  private boolean isPrimaryOwner;
  @JsonProperty("is_restricted")
  private boolean isRestricted;
  @JsonProperty("is_ultra_restricted")
  private boolean isUltraRestricted;
  @JsonProperty("is_bot")
  private boolean isBot;
  @JsonProperty("is_app_user")
  private boolean isAppUser;
  @JsonProperty("has_2fa")
  private boolean has2fa;
  private long updated; // 마지막 업데이트 시간 (UNIX timestamp)
  @JsonProperty("is_email_confirmed") // 명시적으로 매핑
  private boolean isEmailConfirmed; // 이메일 확인 여부.
  private String whoCanShareContactCard; // 프로필 공유 권한 설정.

  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Profile {
    private String avatarHash; // 아바타 해시
    private String statusText; // 상태 메시지
    private String statusEmoji; // 상태 이모지
    private String realName; // 사용자 실명
    private String displayName; // 표시 이름
    private String realNameNormalized; // 정규화된 실명
    private String displayNameNormalized; // 정규화된 표시 이름
    private String email; // 이메일 주소
    @JsonProperty("image_24") // 명시적으로 매핑
    private String image24;
    @JsonProperty("image_32")
    private String image32;
    @JsonProperty("image_48")
    private String image48;
    @JsonProperty("image_72")
    private String image72;
    @JsonProperty("image_192")
    private String image192;
    @JsonProperty("image_512")
    private String image512;
    @JsonProperty("image_1024")
    private String image1024;
    private String imageOriginal; // 추가
    private String team; // 팀 정보
    private boolean isCustomImage; // 추가
    private String title; // 추가
    private String phone; // 추가
    private String skype; // 추가
    private int statusExpiration; // 추가
    private String statusTextCanonical; // 추가
    private List<Object> statusEmojiDisplayInfo; // 추가
    private String firstName; // 추가
    private String lastName; // 추가
  }
}
