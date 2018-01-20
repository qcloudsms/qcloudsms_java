package com.github.qcloudsms;

import org.json.JSONObject;
import org.json.JSONException;

import com.github.qcloudsms.httpclient.HTTPResponse;


public class SmsSingleSenderResult extends SmsResultBase {

    public int result;
    public String errMsg;
    public String ext;
    public String sid;
    public int fee;

    public SmsSingleSenderResult() {
        this.errMsg = "";
        this.ext = "";
        this.sid = "";
        this.fee = 0;
    }

    @Override
    public SmsSingleSenderResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");
        if (json.has("sid")) {
            sid = json.getString("sid");
        }
        if (json.has("fee")) {
            fee = json.getInt("fee");
        }

        return this;
    }
}
