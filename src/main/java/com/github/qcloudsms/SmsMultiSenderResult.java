package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;


public class SmsMultiSenderResult extends SmsResultBase {

    public class Detail {

        public int result;
        public String errmsg = "";
        public String mobile = "";
        public String nationcode = "";
        public String sid = "";
        public int fee = 0;

        public String toString() {
            String[] fields = {"result", "errmsg", "mobile", "nationcode", "sid", "fee"};
            return (new JSONObject(this, fields)).toString();
        }

        public Detail parse(JSONObject json) throws JSONException {

            result = json.getInt("result");
            errmsg = json.getString("errmsg");

            if (json.has("mobile")) {
                mobile = json.getString("mobile");
            }
            if (json.has("nationcode")) {
                nationcode = json.getString("nationcode");
            }
            if (json.has("sid")) {
                sid = json.getString("sid");
            }
            if (json.has("fee")) {
                fee = json.getInt("fee");
            }

            return this;
        }
    }

    public int result;
    public String errMsg;
    public String ext;
    public ArrayList<Detail> details;

    public SmsMultiSenderResult() {
        this.errMsg = "";
        this.ext = "";
        this.details = new ArrayList<Detail>();
    }

    @Override
    public SmsMultiSenderResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (json.has("ext")) {
            ext = json.getString("ext");
        }
        if (json.has("detail") && !json.isNull("detail")) {
            JSONArray jsonDetail = json.getJSONArray("detail");
            for (int i = 0; i < jsonDetail.length(); i++) {
                details.add((new Detail()).parse(jsonDetail.getJSONObject(i)));
            }
        }


        return this;
    }
}
