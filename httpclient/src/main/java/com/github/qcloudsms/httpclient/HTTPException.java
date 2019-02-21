package com.github.qcloudsms.httpclient;


public class HTTPException extends Exception {

    private final int statusCode;
    private final String reason;

    public HTTPException(final int statusCode, final String reason) {
        super("HTTP statusCode: " + statusCode + ", reasons: " + reason);
        this.reason = reason;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }
}
