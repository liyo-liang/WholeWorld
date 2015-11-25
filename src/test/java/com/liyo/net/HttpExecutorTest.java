package com.liyo.net;

import com.liyo.common.HttpResult;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liangyong on 15-11-25.
 */
public class HttpExecutorTest {

    @Test
    public void testDownloadPage() throws Exception {
        HttpResult httpResult = HttpExecutor.downloadPageGet("http://www.baidu.com");
        Assert.assertEquals(HttpStatus.SC_OK, httpResult.getStatusCode());
        Assert.assertTrue(httpResult.getHttpHeaders().length > 0);
        Assert.assertTrue(httpResult.getResponseBody().length > 0);
    }
}
