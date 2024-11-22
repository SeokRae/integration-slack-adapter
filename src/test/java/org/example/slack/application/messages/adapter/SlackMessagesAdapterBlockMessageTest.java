package org.example.slack.application.messages.adapter;

import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.application.model.message.Block;
import org.example.slack.application.model.message.Element;
import org.example.slack.application.model.message.Text;
import org.example.slack.core.props.SlackProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackMessagesAdapterBlockMessageTest {

  @Autowired
  private SlackMessagesAdapter slackMessagesAdapter;
  @Autowired
  private SlackProperties slackProperties;

  @Test
  void block_send_message_테스트() {
    // Given: 블록 메시지 구성
    Block sectionBlock = Block.builder()
      .type(Block.Type.SECTION)
      .text(Text.builder()
        .type(Text.Type.MRKDWN)
        .text("*Welcome to Slack!*")
        .build())
      .build();

    Block dividerBlock = Block.builder()
      .type(Block.Type.DIVIDER)
      .build();

    Block buttonBlock = Block.builder()
      .type(Block.Type.ACTIONS)
      .elements(List.of(Element.builder()
        .type(Element.Type.BUTTON)
        .text(Text.builder()
          .type(Text.Type.PLAIN_TEXT)
          .text("Click Me!")
          .build())
        .actionId("button_click")
        .build()))
      .build();

    List<Block> blocks = List.of(sectionBlock, dividerBlock
      , buttonBlock
    );

    SlackPostMessageRequest postMessageRequest = SlackPostMessageRequest.builder()
      .channel(slackProperties.getGeneralChannelName())
      .blocks(blocks)
      .build();

    // When: 메시지 전송
    SlackMessageResponse response = slackMessagesAdapter.sendMessage(postMessageRequest);

    // Then: Slack API 응답 검증
    assertThat(response).isNotNull();
    assertThat(response.isOk()).isTrue();
    assertThat(response.getChannel()).isNotBlank();
    assertThat(response.getTs()).isNotEmpty();
  }
}