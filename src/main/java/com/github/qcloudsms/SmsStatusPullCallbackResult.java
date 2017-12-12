package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;


public class SmsStatusPullCallbackResult extends SmsResultBase {

    public class Callback {
        // NOTE: Following member variable naming style is just for compatible with old interface
        String user_receive_time;
        String nationcode;
        String mobile;
        String report_status;
        String errmsg;
        String description;
        String sid;

        @Override
        public String toString() {
            String[] fields = {
                "user_receive_time", "nationcode", "mobile",
                "report_status", "errmsg", "description",
                "sid"
            };
            return (new JSONObject(this, fields)).toString();
        }

        public Callback parse(JSONObject json) throws JSONException {

            user_receive_time = json.getString("user_receive_time");
            nationcode = json.getString("nationcode");
            mobile = json.getString("mobile");
            report_status = json.getString("report_status");
            errmsg = json.getString("errmsg");
            description = json.getString("description");
            sid = json.getString("sid");

            return this;
        }
    }

    int result;
    String errMsg;
    int count;
    ArrayList<Callback> callbacks;

    @Override
    public SmsStatusPullCallbackResult  parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (result == 0) {
            count = json.getInt("count");
            if (!json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    callbacks.add((new Callback()).parse(data.getJSONObject(i)));
                }
            }
        }

        return this;
    }
}
