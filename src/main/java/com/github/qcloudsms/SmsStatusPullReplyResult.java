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

        @Override
        public String toString(){
            String[] fields = {"nationcode", "mobile", "text", "sign", "time"};
            return (new JSONObject(this, fields)).toString();
        }

        public Reply parse(JSONObject json) throws JSONException {

            nationcode = json.getString("nationcode");
            mobile = json.getString("mobile");
            text = json.getString("text");
            sign = json.getString("sign");
            time = json.getLong("time");

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
