package com.github.qcloudsms.httpclient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


public class PoolingHTTPClient implements HTTPClient {

    private PoolingHttpClientConnectionManager connMgr;
    private CloseableHttpClient client;

    public PoolingHTTPClient(int maxConnection) {
        this.connMgr = new PoolingHttpClientConnectionManager();
        connMgr.setMaxTotal(maxConnection);

        this.client = HttpClients.custom()
            .setConnectionManager(this.connMgr)
            .build();
    }

    public PoolingHTTPClient() {
        this(100);
    }

    public HTTPResponse fetch(HTTPRequest request) throws IOException, URISyntaxException {
        // Build URI
        URIBuilder uriBuilder = new URIBuilder(request.url);
        for (Map.Entry<String, String> entry: request.parameters.entrySet()) {
            uriBuilder.addParameter(entry.getKey(), entry.getValue());
        }

        // Build request
        RequestConfig reqConfig = RequestConfig.custom()
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

        // Fetch http response
        HttpClientContext ctx = HttpClientContext.create();
        CloseableHttpResponse response = client.execute(reqBuilder.build(), ctx);
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
    }

    public synchronized void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                // in case of a problem or the connection was aborted
            }
            client = null;
        }
        if (connMgr != null) {
            connMgr.close();
            connMgr = null;
        }
    }
}
