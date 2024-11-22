package org.example.slack.common.client;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class CustomRequestBuilder {
    private final Request.Builder builder;

    public CustomRequestBuilder() {
        this.builder = new Request.Builder();
    }

    public CustomRequestBuilder url(String url) {
        builder.url(url);
        return this;
    }

    public CustomRequestBuilder method(String method, RequestBody body) {
        builder.method(method, body);
        return this;
    }

    public CustomRequestBuilder headers(Map<String, String> headers) {
        headers.forEach(builder::addHeader);
        return this;
    }

    public Request build() {
        return builder.build();
    }
}
