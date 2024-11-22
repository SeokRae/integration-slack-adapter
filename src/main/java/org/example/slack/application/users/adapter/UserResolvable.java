package org.example.slack.application.users.adapter;

import org.example.slack.application.users.adapter.request.SlackLookUpByEmailRequest;
import org.example.slack.application.users.adapter.response.SlackLookUpByEmailResponse;

@FunctionalInterface
public interface UserResolvable {
  SlackLookUpByEmailResponse lookUpByEmail(SlackLookUpByEmailRequest request);
}
