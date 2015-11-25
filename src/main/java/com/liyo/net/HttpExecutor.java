package com.liyo.net;

/**
 * Created by liangyong on 15-11-25.
 */

import java.io.IOException;
import java.util.Map;

import com.liyo.common.HttpResult;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpExecutor {
    private static HttpClient httpClient = new HttpClient();

    // 设置代理服务器
    static {
        // 设置代理服务器的IP地址和端口
        //httpClient.getHostConfiguration().setProxy("localhost", 8080);
    }

    /**
     * 使用get方法下载url页面
     *
     * @param pageUrl
     * @return
     * @throws IOException
     */
    public static HttpResult downloadPageGet(String pageUrl) throws IOException {
        GetMethod getMethod = new GetMethod(pageUrl);
        HttpResult httpResult = executeHttpRequest(getMethod);
        return httpResult;
    }

    public static HttpResult downloadPagePost(String pageUrl, Map<String, String> parameterMap) throws IOException {
        PostMethod postMethod = new PostMethod(pageUrl);
        if (parameterMap != null && parameterMap.size() > 0) {
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                postMethod.addParameter(entry.getKey(), entry.getValue());
            }

        }
        return executeHttpRequest(postMethod);

    }

    private static HttpResult executeHttpRequest(HttpMethod httpMethod) throws IOException {
        // 执行，返回状态码
        int statusCode = httpClient.executeMethod(httpMethod);
        HttpResult httpResult = new HttpResult();
        httpResult.setStatusCode(statusCode);
        httpResult.setHttpHeaders(httpMethod.getResponseHeaders());
        // 针对状态码进行处理 (简单起见，只处理返回值为200的状态码)
        if (statusCode == HttpStatus.SC_OK) {
            byte[] responseBody = httpMethod.getResponseBody();
            httpResult.setResponseBody(responseBody);
        }
        return httpResult;
    }
}