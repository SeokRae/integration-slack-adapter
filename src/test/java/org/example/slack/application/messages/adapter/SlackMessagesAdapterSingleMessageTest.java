package org.example.slack.application.messages.adapter;

import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;
import org.example.slack.core.props.SlackProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[슬랙] ")
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = {"default", "prod"})
class SlackMessagesAdapterSingleMessageTest {

  @Autowired
  private SlackMessagesAdapter slackMessagesAdapter;
  @Autowired
  private SlackProperties slackProperties;

  private static Stream<Arguments> sendMessage() {
    return Stream.of(
      Arguments.of("일반 텍스트 메시지", "Simple text message"),
      Arguments.of("이모지 포함 메시지", ":rocket: Message with emoji :tada:"),
      Arguments.of("마크다운 포맷", "*Bold text* _italic_ ~strikethrough~ `code`"),
      Arguments.of("멘션 포함", "<@U123456> Please check this"),
      Arguments.of("링크 포함", "Check this <https://example.com|link>"),
      Arguments.of("블록 포맷", """
          {
              "blocks": [
                  {
                      "type": "section",
                      "text": {
                          "type": "mrkdwn",
                          "text": "*Important Notice*"
                      }
                  }
              ]
          }
          """)
    );
  }

  @DisplayName(value = "다양한 포맷의 메시지 발송 테스트")
  @MethodSource(value = "sendMessage")
  @ParameterizedTest(name = "{index} {displayName} - {0}")
  void 다양한_포맷_메시지_발송_테스트(
    String testCase,
    SlackPostMessageRequest requestMessage
  ) {
    SlackMessageResponse slackMessageResponse = slackMessagesAdapter.sendMessage(requestMessage);

    assertThat(slackMessageResponse)
      .isNotNull()
      .satisfies(response -> {
        assertThat(response.isOk()).isTrue();
        assertThat(response.getChannel()).isNotBlank();
        assertThat(response.getTs()).isNotBlank();
      });
  }
}