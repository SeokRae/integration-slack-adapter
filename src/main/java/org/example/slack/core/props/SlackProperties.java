package org.example.slack.core.props;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Slf4j
@Getter
@ToString
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {

  private final String domain;
  private final Informations informations;
  private final Channels channels;
  private final Uris uris;

  @ConstructorBinding
  public SlackProperties(String domain, Informations informations, Channels channels, Uris uris) {
    this.domain = domain;
    this.informations = informations;
    this.channels = channels;
    this.uris = uris;
    log.info("{}", this);
  }

  /**
   * Slack API Token 반환
   */
  public String getToken() {
    return informations.getToken();
  }

  /**
   * General 채널 이름 반환
   */
  public String getGeneralChannelName() {
    return channels.getGeneral();
  }

  /**
   * Conversations 관련 URI 반환
   */
  public String getCreateConversationUrl() {
    return domain.concat(uris.getConversations().getCreate());
  }

  public String getInviteToConversationUrl() {
    return domain.concat(uris.getConversations().getInvite());
  }

  public String getListConversationsUrl() {
    return domain.concat(uris.getConversations().getList());
  }

  /**
   * Messages 관련 URI 반환
   */
  public String getPostMessageUrl() {
    return domain.concat(uris.getMessages().getPostMessage());
  }

  /**
   * Files 관련 URI 반환
   */
  public String getUploadFileUrl() {
    return domain.concat(uris.getFiles().getUpload());
  }

  @Getter
  @ToString
  public static class Informations {
    private final String token;

    public Informations(String token) {
      this.token = token;
      log.info("{}", this);
    }
  }

  @Getter
  @ToString
  public static class Channels {
    private final String general;

    public Channels(String general) {
      this.general = general;
      log.info("{}", this);
    }
  }

  @Getter
  @ToString
  public static class Uris {
    private final Conversations conversations;
    private final Messages messages;
    private final Files files;

    public Uris(Conversations conversations, Messages messages, Files files) {
      this.conversations = conversations;
      this.messages = messages;
      this.files = files;
      log.info("{}", this);
    }

    @Getter
    @ToString
    public static class Conversations {
      private final String create;
      private final String invite;
      private final String list;

      public Conversations(String create, String invite, String list) {
        this.create = create;
        this.invite = invite;
        this.list = list;
        log.info("{}", this);
      }
    }

    @Getter
    @ToString
    public static class Messages {
      private final String postMessage;

      public Messages(String postMessage) {
        this.postMessage = postMessage;
        log.info("{}", this);
      }
    }

    @Getter
    @ToString
    public static class Files {
      private final String upload;

      public Files(String upload) {
        this.upload = upload;
        log.info("{}", this);
      }
    }
  }
}