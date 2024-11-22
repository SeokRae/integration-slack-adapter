package org.example.slack.application.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Text {

  public enum Type {
    PLAIN_TEXT, MRKDWN;

    @JsonValue
    public String getType() {
      return this.name().toLowerCase();
    }
  }

  @NotBlank(message = "text type is required")
  private Type type;
  @NotBlank(message = "text content is required")
  private String text;
  private Boolean emoji;

}
