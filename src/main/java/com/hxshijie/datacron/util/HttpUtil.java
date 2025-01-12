package com.hxshijie.datacron.util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;

/**
 * 伪装成Chrome浏览器的http请求工具类，已忽略https证书校验便于特殊环境使用
 * @author hxshijie
 * */
public class HttpUtil {

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofDays(20))
            .followRedirects(HttpClient.Redirect.NEVER)
            .version(HttpClient.Version.HTTP_2)
            .sslContext(getSSLContext())
            .build();

    private static final Map<String, String> defaultHeader = new HashMap<>(){{
        put("accept", "*/*");
        put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36");
        put("accept-encoding", "gzip, deflate, br, zstd");
        put("accept-language", "zh-CN,zh;q=0.9");
        put("sec-ch-ua", "\"Google Chrome\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129\"");
        put("sec-ch-ua-mobile", "?0");
        put("sec-ch-ua-platform", "\"Windows\"");
    }};

    /**
     * 发送GET请求
     * @param url 请求的url
     * @param headerMap 额外的请求头，例如token，不需要额外请求头时传入new HashMap<>()
     * @param body 请求body，不需要body时传入空字符串
     * @return 请求的返回值
     * @throws IOException 发送或接收时发生IO错误时抛出
     * @throws InterruptedException 操作被中断时抛出
     * */
    public static String get(String url, Map<String, String> headerMap, String body) throws IOException, InterruptedException {
        String[] header = getHeader(headerMap);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .headers(header)
                .method("GET", HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofDays(20))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * 发送POST请求
     * @param url 请求的url
     * @param headerMap 额外的请求头，例如token，不需要额外请求头时传入new HashMap<>()
     * @param body 请求body，不需要body时传入空字符串
     * @return 请求的返回值
     * @throws IOException 发送或接收时发生IO错误时抛出
     * @throws InterruptedException 操作被中断时抛出
     * */
    public static String post(String url, Map<String, String> headerMap, String body) throws IOException, InterruptedException {
        String[] header = getHeader(headerMap);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .headers(header)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofDays(20))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static SSLContext getSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustManager(), new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager[] getTrustManager() {
        return new X509TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    private static String[] getHeader(Map<String, String> headerMap) {
        Map<String, String> finalHeader = new HashMap<>(defaultHeader);
        finalHeader.putAll(headerMap);
        String[] header = new String[finalHeader.size() * 2];
        int i = 0;
        for (String key : finalHeader.keySet()) {
            header[i] = key;
            header[i + 1] = finalHeader.get(key);
            i = i + 2;
        }
        return header;
    }
}
