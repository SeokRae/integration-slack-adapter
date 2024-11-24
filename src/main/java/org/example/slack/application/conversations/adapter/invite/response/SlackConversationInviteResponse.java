package org.example.slack.application.conversations.adapter.invite.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;
import org.example.slack.application.model.conversation.Channel;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SlackConversationInviteResponse {

    private boolean ok; // 성공 여부

    private String error; // 실패 시 에러 메시지

    private Channel channel; // 성공 시 채널 정보

}
