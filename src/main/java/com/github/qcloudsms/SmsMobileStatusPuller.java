package com.github.qcloudsms;

import org.json.JSONObject;
import org.json.JSONException;

import com.github.qcloudsms.httpclient.HTTPClient;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.HTTPMethod;
import com.github.qcloudsms.httpclient.HTTPRequest;
import com.github.qcloudsms.httpclient.HTTPResponse;
import com.github.qcloudsms.httpclient.DefaultHTTPClient;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;


public class SmsMobileStatusPuller extends SmsBase {

    private String url = "https://yun.tim.qq.com/v5/tlssmssvr/pullstatus4mobile";

    public SmsMobileStatusPuller(int appid, String appkey) {
        super(appid, appkey, new DefaultHTTPClient());
    }

    public SmsMobileStatusPuller(int appid, String appkey, HTTPClient httpclient) {
        super(appid, appkey, httpclient);
    }

    private HTTPResponse pull(int type, String nationCode, String mobile, long beginTime,
            long endTime, int max) throws IOException {

        long random = SmsSenderUtil.getRandom();
        long now = SmsSenderUtil.getCurrentTime();
        JSONObject body = new JSONObject();
        body.put("sig", SmsSenderUtil.calculateSignature(this.appkey, random, now))
            .put("type", type)
            .put("time", now)
            .put("max", max)
            .put("begin_time", beginTime)
            .put("end_time", endTime)
            .put("nationcode", nationCode)
            .put("mobile", mobile);

        HTTPRequest req = new HTTPRequest(HTTPMethod.POST, this.url)
            .addHeader("Conetent-Type", "application/json")
            .addQueryParameter("sdkappid", this.appid)
            .addQueryParameter("random", random)
            .setConnectionTimeout(60 * 1000)
            .setRequestTimeout(60 * 10000)
            .setBody(body.toString());

        // May throw IOException
        try {
            return httpclient.fetch(req);
        } catch(URISyntaxException e) {
            throw new RuntimeException("API url has been modified, current url: " + url);
        }
    }

    public SmsStatusPullCallbackResult pullCallback(String nationCode, String mobile,
        long beginTime, long endTime, int max)
            throws HTTPException, JSONException, IOException {

        // May throw IOException
        HTTPResponse res = pull(0, nationCode, mobile, beginTime, endTime, max);

        // May throw HTTPException
        handleError(res);

        // May throw JSONException
        return (new SmsStatusPullCallbackResult()).parseFromHTTPResponse(res);
    }

    public SmsStatusPullReplyResult pullReply(String nationCode, String mobile,
        long beginTime, long endTime, int max)
            throws HTTPException, JSONException, IOException {

        // May throw IOException
        HTTPResponse res = pull(1, nationCode, mobile, beginTime, endTime, max);

        // May throw HTTPException
        handleError(res);

        // May throw JSONException
        return (new SmsStatusPullReplyResult()).parseFromHTTPResponse(res);
    }
}
