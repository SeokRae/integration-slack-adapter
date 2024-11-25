package org.example.slack.application.files.adapter.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SlackFileUploadResponse {
    private boolean ok;
    private String error;
    private FileInfo file;

    @Getter
    @ToString
    public static class FileInfo {
        private String id;
        private Long created;
        private String name;
        private String title;
        private String mimetype;
        private String filetype;
        private String prettyType;
        private String user;
        private String urlPrivate;
        private String urlPrivateDownload;
        private String permalink;
        private String permalinkPublic;
    }
} 