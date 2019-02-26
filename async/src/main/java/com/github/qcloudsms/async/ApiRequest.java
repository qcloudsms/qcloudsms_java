package com.github.qcloudsms.async;

interface ApiRequest {
    public long random();
    public String url();
    public String body(String appkey);
}
