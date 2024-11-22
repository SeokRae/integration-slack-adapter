package org.example.slack.application.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Element {

  public enum Type {
    BUTTON, IMAGE, DATEPICKER, STATIC_SELECT, OVERFLOW;

    @JsonValue
    public String getType() {
      return this.name().toLowerCase();
    }
  }

  @NotBlank(message = "element type is required")
  private Type type;
  private Text text;
  private String actionId;
  private String url;
  private String style;
  private String value;
}
