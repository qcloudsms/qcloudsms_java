package com.github.qcloudsms.async;

import org.json.JSONObject;
import org.json.JSONException;


public class SingleSenderRequest implements ApiRequest {

    private String nationcode_;
    private String phone_;
    private String extend_;
    private String ext_;

    private int type_;
    private String msg_;

    private int templateId_;
    private String[] params_;
    private String sign_;

    private long random_;

    private String url_ = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";

    public SingleSenderRequest() {
        nationcode_ = "86";
        type_ = 0;
        extend_ = "";
        ext_ = "";
        templateId_ = -1;
        random_ = SmsSenderUtil.getRandom();
    }

    public SingleSenderRequest nationcode(String v) {
        this.nationcode_ = v;
        return this;
    }

    public SingleSenderRequest phone(String v) {
        this.phone_ = v;
        return this;
    }

    public SingleSenderRequest type(int v) {
        if (v == 1) {
            this.type_ = 1;
        } else {
            this.type_ = 0;
        }
        return this;
    }
    public SingleSenderRequest msg(String v) {
        this.msg_ = v;
        return this;
    }

    public SingleSenderRequest extend(String v) {
        this.extend_ = v;
        return this;
    }

    public SingleSenderRequest ext(String v) {
        this.ext_ = v;
        return this;
    }

    public SingleSenderRequest templateId(int v) {
        this.templateId_ = v;
        return this;
    }

    public SingleSenderRequest params(String[] v) {
        this.params_ = v;
        return this;
    }

    public SingleSenderRequest sign(String v) {
        this.sign_ = v;
        return this;
    }

    public String url() {
        return this.url_;
    }

    public String body(String key) {
        long now = SmsSenderUtil.getCurrentTime();

        JSONObject body = new JSONObject()
            .put("tel", (new JSONObject()).put("nationcode", nationcode_).put("mobile", phone_))
            .put("sig", SmsSenderUtil.calculateSignature(key, random_, now, phone_))
            .put("time", now)
            .put("extend", extend_)
            .put("ext", ext_);

        if (templateId_ > 0) {
            body.put("tpl_id", templateId_)
                .put("params", params_)
                .put("sign", sign_);
        } else {
            body.put("type", type_)
                .put("msg", msg_);
        }

        return body.toString();
    }

    public long random() {
        return this.random_;
    }
}
