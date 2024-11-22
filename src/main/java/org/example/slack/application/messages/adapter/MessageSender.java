package org.example.slack.application.messages.adapter;

import org.example.slack.application.messages.adapter.request.SlackPostMessageRequest;
import org.example.slack.application.messages.adapter.response.SlackMessageResponse;

@FunctionalInterface
public interface MessageSender {
  SlackMessageResponse sendMessage(SlackPostMessageRequest request);
}