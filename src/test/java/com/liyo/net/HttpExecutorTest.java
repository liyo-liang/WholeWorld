package com.liyo.net;

import com.liyo.common.HttpResult;
import com.liyo.html.HTMLNode;
import com.liyo.html.HTMLParser;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Created by liangyong on 15-11-25.
 */
public class HttpExecutorTest {

    @Test
    public void testDownloadPage() throws Exception {
        HttpResult httpResult = HttpExecutor.downloadPageGet("http://www.baidu.com");
        Assert.assertEquals(HttpStatus.SC_OK, httpResult.getStatusCode());
        Assert.assertTrue(httpResult.getHttpHeaders().length > 0);
        Assert.assertNotNull(httpResult.getResponseBody());

        httpResult = HttpExecutor.downloadPagePost("http://www.baidu.com", null);
        Assert.assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, httpResult.getStatusCode());
        Assert.assertTrue(httpResult.getHttpHeaders().length > 0);
        Assert.assertNull(httpResult.getResponseBody());
        boolean isLocation = false;
        for(Header header: httpResult.getHttpHeaders()){
            if(StringUtils.equalsIgnoreCase(header.getName(), "location")){
                isLocation = true;
            }
        }
        Assert.assertTrue(isLocation);
    }

    @Test
    public void testHtmlParser() throws Exception{
        String url = "http://www.sohu.com";
        HttpResult httpResult = HttpExecutor.downloadPageGet(url);
        Assert.assertEquals(HttpStatus.SC_OK, httpResult.getStatusCode());
        Assert.assertTrue(httpResult.getHttpHeaders().length > 0);
        Assert.assertNotNull(httpResult.getResponseBody());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(httpResult.getResponseBody());
        for(Header header: httpResult.getHttpHeaders()){
            System.out.println(header);
        }
        HTMLParser htmlParser = new HTMLParser(byteArrayInputStream, url, null);
        Enumeration  htmlEnumeration = htmlParser.elements();
        while(htmlEnumeration.hasMoreElements()){
            HTMLNode htmlNode = (HTMLNode)htmlEnumeration.nextElement();
            htmlNode.print();
        }
    }
}
