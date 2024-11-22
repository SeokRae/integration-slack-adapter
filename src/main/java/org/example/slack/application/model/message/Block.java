package org.example.slack.application.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {

  public enum Type {
    SECTION, DIVIDER, CONTEXT, ACTIONS, HEADER, IMAGE;

    @JsonValue
    public String getType() {
      return this.name().toLowerCase();
    }
  }

  @NotBlank(message = "block type is required")
  private Type type;
  private Text text;
  private List<Element> elements;
  private Accessory accessory;
}
