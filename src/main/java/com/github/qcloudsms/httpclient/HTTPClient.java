package com.github.qcloudsms.httpclient;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * HTTPClient interface
 *
 * @since 1.0.0
 */
public interface HTTPClient {

    /**
     * Fetch HTTP response by given HTTP request,
     *
     * @param request  Specify which to be fetched.
     *
     * @return the response to the request.
     * @throws IOException  connection problem.
     * @throws HTTPException http exception
     */
    HTTPResponse fetch(HTTPRequest request)
        throws IOException, URISyntaxException;

    /**
     * Close http client and release resource
     *
     * @return void
     */
    void close();
}
