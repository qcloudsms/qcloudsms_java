package com.github.qcloudsms.async;

import com.github.qcloudsms.httpclient.HTTPRequest;
import com.github.qcloudsms.httpclient.HTTPResponse;
import com.github.qcloudsms.httpclient.HTTPTracing;
import com.github.qcloudsms.httpclient.ResponseHandler;


public class RequestInfo {

    public HTTPRequest request;
    public HTTPResponse response;
    public HTTPTracing tracing;
    protected ResponseHandler handler;

    public RequestInfo(HTTPRequest request, ResponseHandler handler) {
        this.request = request;
        this.response = new HTTPResponse(request);
        this.tracing = new HTTPTracing(System.currentTimeMillis());
        this.handler = handler;
    }
}
