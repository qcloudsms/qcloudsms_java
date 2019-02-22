package com.github.qcloudsms.async;

import com.github.qcloudsms.httpclient.AsyncHTTPClient;
import com.github.qcloudsms.httpclient.HTTPRequest;
import com.github.qcloudsms.httpclient.HTTPMethod;
import com.github.qcloudsms.httpclient.ResponseHandler;

import java.io.IOException;


public class QcloudSms {

    protected int appid;
    protected String appkey;
    protected AsyncHTTPClient client;
    protected boolean inited;

    private static QcloudSms instance;

    public QcloudSms() {
        client = null;
        inited = false;
    }

    public static synchronized QcloudSms getInstance() {
        if (instance == null) {
            instance = new QcloudSms();
        }
        return instance;
    }

    public void init(int appid, String appkey) throws IOException {
        init(appid, appkey, new NettyHTTPClient());
    }

    public void init(int appid, String appkey, AsyncHTTPClient client) throws IOException {
        if (client == null) {
            this.client = new NettyHTTPClient();
        } else {
            this.client = client;
        }

        this.client.init();
        this.appid = appid;
        this.appkey = appkey;

        inited = true;
    }

    public void submit(UserRequest request, ResponseHandler handler) {
        assert inited;

        HTTPRequest req = new HTTPRequest(HTTPMethod.POST, request.url())
            .addHeader("Conetent-Type", "application/json")
            .addQueryParameter("sdkappid", this.appid)
            .addQueryParameter("random", request.random())
            .setBody(request.body(this.appkey));

        this.client.submit(req, handler);
    }

    public void close() {
        this.client.close();
    }
}
