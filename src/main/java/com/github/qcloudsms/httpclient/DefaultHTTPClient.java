package com.github.qcloudsms.httpclient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


public class DefaultHTTPClient implements HTTPClient {

    public HTTPResponse fetch(HTTPRequest request) throws IOException, URISyntaxException {
        // Build URI
        URIBuilder uriBuilder = new URIBuilder(request.url);
        for (Map.Entry<String, String> entry: request.parameters.entrySet()) {
            uriBuilder.addParameter(entry.getKey(), entry.getValue());
        }

        // Build request
        RequestConfig reqConfig = RequestConfig.custom()
            .setSocketTimeout(5 * 60 * 1000)
            .setConnectTimeout(request.connectTimeout)
            .setConnectionRequestTimeout(request.requestTimeout)
            .build();
        RequestBuilder reqBuilder = RequestBuilder.create(request.method.name())
            .setUri(uriBuilder.build())
            .setEntity(new StringEntity(request.body, "UTF-8"))
            .setConfig(reqConfig);
        for (Map.Entry<String, String> entry: request.headers.entrySet()) {
            reqBuilder.setHeader(entry.getKey(), entry.getValue());
        }

        // Create http client
        CloseableHttpClient client = HttpClients.createDefault();

        // Fetch http response
        try {
            CloseableHttpResponse response = client.execute(reqBuilder.build());
            try {
                // May throw IOException
                HTTPResponse res = new HTTPResponse()
                    .setRequest(request)
                    .setStatusCode(response.getStatusLine().getStatusCode())
                    .setReason(response.getStatusLine().getReasonPhrase())
                    .setBody(EntityUtils.toString(response.getEntity(), "UTF-8"));
                for (Header header: response.getAllHeaders()) {
                    res.addHeader(header.getName(), header.getValue());
                }

                return res;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public void close() {
        // Do nothing
    }
}
