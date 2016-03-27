package com.brucefan.asyncHttp;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;

/**
 * 异步+连接池httpclient工具类
 * Created by bruce01.fan on 2015/6/24.
 */
public class HttpAsynClientUtil {
    //private static final Logger logger = LoggerFactory.getLogger(HttpAsynClientUtil.class);
    public final static String charset = "UTF-8";
    public static final int DEFAULT_TIMEOUT = 1;
    public static int DEFAULT_CONNECTION_TIMEOUT = 200;
    public static int DEFAULT_SOCKET_TIMEOUT = 1000;
    public static final int DEFAULT_MAX_TOTAL = 20000;
    public static final int DEFAULT_MAX_PER_ROUTE = 2000;
    public static int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 150;

    private static CloseableHttpAsyncClient httpAsyncClient;

    private HttpAsynClientUtil() {
    }

    static {
        initClient();
    }

    public static void doPost(String url2Request, String name, String json) {
        final HttpPost post = new HttpPost(url2Request);
        StringBuffer jsonContent = new StringBuffer().append("{").append(json).append("}");
        //设置urlencode
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(name, jsonContent.toString()));
            post.setEntity(new UrlEncodedFormEntity(params, charset));
        } catch (UnsupportedEncodingException e) {
            //logger.error("scLog json content translate URLencode utf8 error", jsonContent);
        }
        httpAsyncClient.execute(post, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                System.out.println("success");
            }

            @Override
            public void failed(Exception e) {
                // logger.warn("[HttpAsynClientUtil] " + post.getRequestLine() + " -> be failed,params -> " + json, e);
            }

            @Override
            public void cancelled() {
                // logger.warn("[HttpAsynClientUtil] " + post.getRequestLine() + " -> be cancelled,params -> " + json);
            }
        });
    }

    private static void initClient() {
        /*
            设置连接池及初始化httpAsyncClient
         */
        try {
            Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE).build();

            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor, sessionStrategyRegistry);
            // 设置socket的timeout 与 连接timeout
            RequestConfig config = RequestConfig.custom()
                    .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                    .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                    .build();
            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom()
                    .setMaxHeaderCount(200)
                    .setMaxLineLength(2000)
                    .build();
            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints)
                    .build();

            connectionManager.setDefaultConnectionConfig(connectionConfig);

            // 设置连接池的属性
            connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL); // 连接池最大数
            connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE); // 每个路由基础连接数

            httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
            httpAsyncClient.start();

        } catch (IOReactorException e) {
            // logger.error("[HttpAsynClientUtil] IO Reactor exception.", e);
        }
    }


    public static void main(String[] args) {
        HttpAsynClientUtil.doPost("http://sc.vip.com", "hi", "{data:success}");
    }
}
