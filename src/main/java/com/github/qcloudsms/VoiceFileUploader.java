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
import java.nio.charset.StandardCharsets;
import java.net.URISyntaxException;


public class VoiceFileUploader extends SmsBase {

    public enum ContentType {
        WAV, MP3
    }

    private String url = "https://cloud.tim.qq.com/v5/tlsvoicesvr/uploadvoicefile";

    public VoiceFileUploader(int appid, String appkey) {
        super(appid, appkey, new DefaultHTTPClient());
    }

    public VoiceFileUploader(int appid, String appkey, HTTPClient httpclient) {
        super(appid, appkey, httpclient);
    }

    /**
     * 上传语音文件
     *
     * @param fileContent  语音文件内容
     * @param contentType  语音文件类型
     * @return {@link}VoiceFileUploaderResult
     * @throws HTTPException  http status exception
     * @throws JSONException  json parse exception
     * @throws IOException    network problem
     */
    public VoiceFileUploaderResult upload(byte[] fileContent, ContentType contentType)
            throws HTTPException, JSONException, IOException {

        long random = SmsSenderUtil.getRandom();
        long now = SmsSenderUtil.getCurrentTime();
        String fileSha1Sum = SmsSenderUtil.sha1sum(fileContent);
        String auth = SmsSenderUtil.calculateAuth(this.appkey, random, now, fileSha1Sum);
        String type;
        if (contentType == ContentType.WAV) {
            type = "audio/wav";
        } else {
            type = "audio/mpeg";
        }

        HTTPRequest req = new HTTPRequest(HTTPMethod.POST, this.url)
            .addHeader("Content-Type", type)
            .addHeader("x-content-sha1", fileSha1Sum)
            .addHeader("Authorization", auth)
            .addQueryParameter("sdkappid", this.appid)
            .addQueryParameter("random", random)
            .addQueryParameter("time", now)
            .setConnectionTimeout(60 * 1000)
            .setRequestTimeout(60 * 1000)
            .setBody(new String(fileContent, StandardCharsets.ISO_8859_1))
            .setBodyCharset(StandardCharsets.ISO_8859_1);

        try {
            // May throw IOException and URISyntaxexception
            HTTPResponse res = httpclient.fetch(req);

            // May throw HTTPException
            handleError(res);

            // May throw JSONException
            return (new VoiceFileUploaderResult()).parseFromHTTPResponse(res);
        } catch(URISyntaxException e) {
            throw new RuntimeException("API url has been modified, current url: " + url);
        }
    }
}
