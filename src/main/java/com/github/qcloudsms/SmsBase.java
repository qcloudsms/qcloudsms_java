package com.github.qcloudsms;

import com.github.qcloudsms.httpclient.HTTPClient;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.HTTPResponse;


public class SmsBase {

    protected int appid;
    protected String appkey;
    protected HTTPClient httpclient;

    /**
     * SmsBase constructor
     *
     * @param appid   sdk appid
     * @param appkey  sdk appkey
     * @param httpclient  http client
     */
    public SmsBase(int appid, String appkey, HTTPClient httpclient) {
        this.appid = appid;
        this.appkey = appkey;
        this.httpclient = httpclient;
    }

    /**
     * Handle http status error
     *
     * @param response   raw http response
     * @return response  raw http response
     * @throws HTTPException  http status exception
     */
    public HTTPResponse handleError(HTTPResponse response) throws HTTPException {
        if (response.statusCode < 200 || response.statusCode >= 300) {
            throw new HTTPException(response.statusCode, response.reason);
        }
        return response;
    }
}
