package com.github.qcloudsms.httpclient;

import java.io.IOException;


/**
 * HTTPClient interface
 *
 * @since 2.0.0
 */
public abstract class AsyncHTTPClient {

    public void init() throws IOException {
    }

    public abstract int submit(final HTTPRequest request, final ResponseHandler handler);

    public void close() {
    }
}
