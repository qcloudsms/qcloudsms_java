package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPClient;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.HTTPMethod;
import com.github.qcloudsms.httpclient.HTTPRequest;
import com.github.qcloudsms.httpclient.HTTPResponse;
import com.github.qcloudsms.httpclient.DefaultHTTPClient;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;


public class SmsStatusPuller extends SmsBase {

    private String url = "https://yun.tim.qq.com/v5/tlssmssvr/pullstatus";

    public SmsStatusPuller(int appid, String appkey) {
        super(appid, appkey, new DefaultHTTPClient());
    }

    public SmsStatusPuller(int appid, String appkey, HTTPClient httpclient) {
        super(appid, appkey, httpclient);
    }

    private HTTPResponse pull(int type, int max) throws IOException {

        long random = SmsSenderUtil.getRandom();
        long now = SmsSenderUtil.getCurrentTime();
        JSONObject body = new JSONObject()
            .put("sig", SmsSenderUtil.calculateSignature(this.appkey, random, now))
            .put("time", now)
            .put("type", type)
            .put("max", max);

        HTTPRequest req = new HTTPRequest(HTTPMethod.POST, this.url)
            .addHeader("Conetent-Type", "application/json")
            .addQueryParameter("sdkappid", this.appid)
            .addQueryParameter("random", random)
            .setConnectionTimeout(60 * 1000)
            .setRequestTimeout(60 * 10000)
            .setBody(body.toString());

        try {
            // May throw IOException and URISyntaxexception
            return httpclient.fetch(req);
        } catch(URISyntaxException e) {
            throw new RuntimeException("API url has been modified, current url: " + url);
        }
    }

    /**
     * 拉取回执结果
     *
     * @param max  最大条数, 最多100
     * @return {@link}SmsStatusPullCallbackResult
     * @throws HTTPException  http status exception
     * @throws JSONException  json parse exception
     * @throws IOException    network problem
     */
    public SmsStatusPullCallbackResult pullCallback(int max)
            throws HTTPException, JSONException, IOException {

        // May throw IOException
        HTTPResponse res = pull(0, max);

        // May throw HTTPException
        handleError(res);

        // May throw JSONException
        return (new SmsStatusPullCallbackResult()).parseFromHTTPResponse(res);
    }


    /**
     * 拉取回复信息
     *
     * @param max  最大条数, 最多100
     * @return {@link}SmsStatusPullReplyResult
     * @throws HTTPException  http status exception
     * @throws JSONException  json parse exception
     * @throws IOException    network problem
     */
    public SmsStatusPullReplyResult pullReply(int max)
            throws HTTPException, JSONException, IOException {

        // May throw IOException
        HTTPResponse res = pull(1, max);

        // May throw HTTPException
        handleError(res);

        // May throw JSONException
        return (new SmsStatusPullReplyResult()).parseFromHTTPResponse(res);
    }
}
