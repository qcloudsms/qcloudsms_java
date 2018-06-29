package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONObject;
import org.json.JSONException;


public class VoiceFileUploaderResult extends SmsResultBase {

    public int result;
    public String errMsg;
    public String fid;

    public VoiceFileUploaderResult() {
        this.errMsg = "";
        this.fid = "";
    }

    @Override
    public VoiceFileUploaderResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (json.has("fid")) {
            fid = json.getString("fid");
        }

        return this;
    }
}
