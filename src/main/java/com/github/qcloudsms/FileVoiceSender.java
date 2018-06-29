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
import java.util.ArrayList;


public class FileVoiceSender extends SmsBase {

    private String url = "https://cloud.tim.qq.com/v5/tlsvoicesvr/sendfvoice";

    public FileVoiceSender(int appid, String appkey) {
        super(appid, appkey, new DefaultHTTPClient());
    }

    public FileVoiceSender(int appid, String appkey, HTTPClient httpclient) {
        super(appid, appkey, httpclient);
    }

    /**
     * 发送文件语音
     *
     * @param nationCode  国家码，如 86 为中国
     * @param phoneNumber  不带国家码的手机号
     * @param fid  语音文件fid
     * @param playtimes  播放次数
     * @param ext  扩展字段，原样返回
     * @return {@link}FileVoiceSenderResult
     * @throws HTTPException  http status exception
     * @throws JSONException  json parse exception
     * @throws IOException    network problem
     */
    public FileVoiceSenderResult send(String nationCode, String phoneNumber,
        String fid, int playtimes, String ext)
            throws HTTPException, JSONException, IOException {

        long random = SmsSenderUtil.getRandom();
        long now = SmsSenderUtil.getCurrentTime();
        JSONObject body = new JSONObject()
            .put("tel", (new JSONObject()).put("nationcode", nationCode).put("mobile", phoneNumber))
            .put("fid", fid)
            .put("playtimes", playtimes)
            .put("sig", SmsSenderUtil.calculateSignature(this.appkey, random, now, phoneNumber))
            .put("time", now)
            .put("ext", SmsSenderUtil.isNotEmpty(ext) ? ext : "");

        HTTPRequest req = new HTTPRequest(HTTPMethod.POST, this.url)
            .addHeader("Conetent-Type", "application/json")
            .addQueryParameter("sdkappid", this.appid)
            .addQueryParameter("random", random)
            .setConnectionTimeout(60 * 1000)
            .setRequestTimeout(60 * 1000)
            .setBody(body.toString());

        try {
            // May throw IOException and URISyntaxexception
            HTTPResponse res = httpclient.fetch(req);

            // May throw HTTPException
            handleError(res);

            // May throw JSONException
            return (new FileVoiceSenderResult()).parseFromHTTPResponse(res);
        } catch(URISyntaxException e) {
            throw new RuntimeException("API url has been modified, current url: " + url);
        }
    }
}
