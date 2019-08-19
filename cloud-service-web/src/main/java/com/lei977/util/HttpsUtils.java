package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.util.LogbackLogger;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2016
 * 说明:https请求方式
 * 项目名称: cloud-tdp-moudles
 * 创建日期: 2017/1/5 12:44
 * 作者: zl
 */
public class HttpsUtils {
    public static String httpsGet(String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Request Url can not be empty");
        }
        String body = null;
        HttpClient client = new SSLClient();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                body = EntityUtils.toString(entity, "UTF-8");
                if (StringUtils.isNotBlank(body)) ;
                {
                    return body;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public static String httpPost(String postUrl, JSONObject jsonObject) {
        if (StringUtils.isBlank(postUrl)) {
            throw new IllegalArgumentException("Request Url can not be empty");
        }
        String body = null;
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(jsonObject), "utf-8"));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity, "utf-8");
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 新锐专用
     *
     * @param postUrl
     * @param jsonObject
     * @param token
     * @return
     */
    public static String postJson(String postUrl, JSONObject jsonObject, String token) {
        if (StringUtils.isBlank(postUrl)) {
            throw new IllegalArgumentException("Request Url can not be empty");
        } else {
            String body = null;
            HttpsUtils.SSLClient httpClient = new HttpsUtils.SSLClient();
            HttpPost httpPost = new HttpPost(postUrl);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(jsonObject), "utf-8"));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("token", token);
            httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity e = response.getEntity();
                body = EntityUtils.toString(e, "utf-8");
                return body;
            } catch (Exception var7) {
                var7.printStackTrace();
                return body;
            }
        }
    }

    public static String httpPostJson(String postUrl, JSONObject jsonObject) {
        if (StringUtils.isBlank(postUrl)) {
            throw new IllegalArgumentException("Request Url can not be empty");
        }
        String body = null;
        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(jsonObject), "utf-8"));
        httpPost.setHeader("Content-Type", "application/json");
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity, "utf-8");
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    public static String httpsPut(String putUrl, JSONObject jsonObject) {
        if (StringUtils.isBlank(putUrl)) {
            throw new IllegalArgumentException("Request Url is null");
        }
        String body = null;
        HttpClient httpClient = new SSLClient();
        HttpPut put = new HttpPut(putUrl);
        put.setHeader("content-type", "application/x-www-form-urlencoded");
        List params = new ArrayList();
        if (jsonObject != null) {
            for (Map.Entry<String, Object> keys : jsonObject.entrySet()) {
                params.add(new BasicNameValuePair(keys.getKey(), Objects.toString(keys.getValue())));
            }
        }
        try {
            put.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpClient.execute(put);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity, "UTF-8");
                    return body;
                }
            }
        } catch (Exception e) {
            LogbackLogger.error(e);
        }
        return body;
    }

    static class SSLClient extends DefaultHttpClient {

        public SSLClient() {
            super();
            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            try {
                ctx.init(null, new TrustManager[]{tm}, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }
}
