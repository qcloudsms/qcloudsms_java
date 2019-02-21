package com.github.qcloudsms.httpclient;


public abstract class ResponseHandler {

    public abstract void onResponse(HTTPResponse response, HTTPTracing tracing);

    public void onError(Throwable error, HTTPTracing tracing) {
        // Do nothing
    }
}
