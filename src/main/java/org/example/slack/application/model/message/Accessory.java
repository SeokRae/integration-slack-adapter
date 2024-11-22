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
public class Accessory {

  public enum Type {
    IMAGE, BUTTON, DATEPICKER, STATIC_SELECT;

    @JsonValue
    public String getType() {
      return this.name().toLowerCase();
    }
  }

  @NotBlank(message = "accessory type is required")
  private Type type;
  private String imageUrl;
  private String altText;

}
