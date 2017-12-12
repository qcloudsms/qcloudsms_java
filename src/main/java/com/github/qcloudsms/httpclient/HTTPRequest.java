package com.github.qcloudsms.httpclient;

import java.util.HashMap;


public class HTTPRequest {

    public String url;
    public HTTPMethod method;
    public String body;
    public HashMap<String, String> headers;
    public HashMap<String, String> parameters;
    public int connectTimeout;
    public int requestTimeout;

    public HTTPRequest(final HTTPMethod method, final String url) {
        super();
        this.method = method;
        this.url = url;
    }

    public HTTPRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public HTTPRequest addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public HTTPRequest addQueryParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public HTTPRequest addQueryParameter(String name, int value) {
        parameters.put(name, String.valueOf(value));
        return this;
    }

    public HTTPRequest addQueryParameter(String name, long value) {
        parameters.put(name, String.valueOf(value));
        return this;
    }

    public HTTPRequest setConnectionTimeout(int seconds) {
        connectTimeout = seconds;
        return this;
    }

    public HTTPRequest setRequestTimeout(int seconds) {
        requestTimeout = seconds;
        return this;
    }
}
