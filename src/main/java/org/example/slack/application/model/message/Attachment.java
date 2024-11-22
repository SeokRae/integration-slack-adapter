package org.example.slack.application.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Attachment {
  private String fallback;
  private String color;
  private String pretext;
  private String authorName;
  private String authorLink;
  private String authorIcon;
  private String title;
  private String titleLink;
  private String text;
  private String imageUrl;
  private String thumbUrl;
  private String footer;
  private String footerIcon;
  private String ts;
  private List<Field> fields;

  @Getter
  @Builder
  @ToString
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Field {
    private String title;
    private String value;
    private Boolean shortField;

  }
}
