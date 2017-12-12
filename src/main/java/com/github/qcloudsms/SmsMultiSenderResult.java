package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;



public class SmsMultiSenderResult extends SmsResultBase {

    class Detail {

        public int result;
        public String errMsg = "";
        public String phoneNumber = "";
        public String nationCode = "";
        public String sid = "";
        public int fee;

        public String toString() {
            String[] fields = {"result", "errMsg", "phoneNumber", "nationCode", "sid", "fee"};
            return (new JSONObject(this, fields)).toString();
        }

        public Detail parse(JSONObject json) throws JSONException {

            result = json.getInt("result");
            errMsg = json.getString("errmsg");

            if (result == 0) {
                phoneNumber = json.getString("phoneNumber");
                nationCode = json.getString("nationCode");
                sid = json.getString("sid");
                fee = json.getInt("fee");
            }

            return this;
        }
    }

    public int result;
    public String errMsg = "";
    public String ext = "";
    public ArrayList<Detail> details;

    @Override
    public SmsMultiSenderResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);
        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (result == 0) {
            ext = json.getString("ext");
            if (!json.isNull("detail")) {
                JSONArray jsonDetail = json.getJSONArray("detail");
                for (int i = 0; i < jsonDetail.length(); i++) {
                    details.add((new Detail()).parse(jsonDetail.getJSONObject(i)));
                }
            }
        }

        return this;
    }
}
