package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONObject;
import org.json.JSONException;


public class VoiceFileStatusResult extends SmsResultBase {

    public int result;
    public String errMsg;
    public int status;

    public VoiceFileStatusResult() {
        this.errMsg = "";
    }

    @Override
    public VoiceFileStatusResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (json.has("status")) {
            status = json.getInt("status");
        }

        return this;
    }
}
