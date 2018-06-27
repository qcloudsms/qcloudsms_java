package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONObject;
import org.json.JSONException;


public class VoiceFileUploaderResult extends SmsResultBase {

    public int result;
    public String errMsg;
    public String ext;
    public String fid;

    public VoiceFileUploaderResult() {
        this.errMsg = "";
        this.ext = "";
        this.fid = "";
    }

    @Override
    public VoiceFileUploaderResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (json.has("ext")) {
            ext = json.getString("ext");
        }
        if (json.has("fid")) {
            fid = json.getString("fid");
        }

        return this;
    }
}
