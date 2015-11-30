package com.liyo.common;

import org.apache.commons.httpclient.Header;

import java.util.Arrays;

/**
 * Created by liangyong on 15-11-25.
 */
public class HttpResult {
    private int statusCode;
    private Header[] httpHeaders;
    private byte[] responseBody;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Header[] getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Header[] httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HttpResult that = (HttpResult) o;

        if (statusCode != that.statusCode) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(httpHeaders, that.httpHeaders)) return false;
        return Arrays.equals(responseBody, that.responseBody);

    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (httpHeaders != null ? Arrays.hashCode(httpHeaders) : 0);
        result = 31 * result + (responseBody != null ? Arrays.hashCode(responseBody) : 0);
        return result;
    }
}
