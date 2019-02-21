package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;


public class SmsStatusPullReplyResult extends SmsResultBase {

    public class Reply {

        public String nationcode;
        public String mobile;
        public String text;
        public String sign;
        public long time;
        public String extend;

        @Override
        public String toString(){
            String[] fields = {"nationcode", "mobile", "text", "sign", "time", "extend"};
            return (new JSONObject(this, fields)).toString();
        }

        public Reply parse(JSONObject json) throws JSONException {

            if (json.has("nationcode")) {
                nationcode = json.getString("nationcode");
            }
            if (json.has("mobile")) {
                mobile = json.getString("mobile");
            }
            if (json.has("text")) {
                text = json.getString("text");
            }
            if (json.has("text")) {
                sign = json.getString("text");
            }
            if (json.has("time")) {
                time = json.getLong("time");
            }
            if (json.has("extend")) {
                extend = json.getString("extend");
            }

            return this;
        }
    }

    public int result;
    public String errMsg;
    public int count;
    public ArrayList<Reply> replys;

    public SmsStatusPullReplyResult() {
        this.errMsg = "";
        this.count = 0;
        this.replys = new ArrayList<Reply>();
    }

    @Override
    public SmsStatusPullReplyResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");
        if (json.has("count")) {
            count = json.getInt("count");
        }

        if (json.has("data") && !json.isNull("data")) {
            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                replys.add((new Reply()).parse(data.getJSONObject(i)));
            }
        }

        return this;
    }
}
