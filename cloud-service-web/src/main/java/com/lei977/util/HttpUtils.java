package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static void main(String args[]) {
//        System.out.println(httpGet("http://api.geatmap.com/geoutils/partitions?token=57ad6682cc085b0718b6ca66&shape=121.4630660000,29.9973610000|121.4604160000,29.9983850000|121.4589920000,29.9962660000|121.4557990000,29.9965160000&precision=8"));
        System.out.println(httpGet("http://192.168.11.202:11187/tenant/updateTenantTopic?id=10df7f1ba9344d819ec355333442d264&tenantId=13d09a32357143bbaa781fb5ed8eb556&app_theme=backTheme"));
    }

    public static String httpGet(String get_url) {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URL url = new URL(get_url);
            URI uri = new URI(url.getProtocol(), (url.getHost() + (url.getPort() == -1 ? "" : ":" + url.getPort())), url.getPath(), url.getQuery(), null);
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response;
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String findLogoByIP(String u, String p) {
        String result = httpGet("http://192.168.11.202:11187/tenant/queryByDomainPort?domain=" + u + "&port=" + p);
        return result;
    }

    public static String postJson(String url, JSONObject parms) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(parms), "utf-8"));
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response;
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            //防止中文乱码，统一utf-8格式
            String body = EntityUtils.toString(entity,"UTF-8");
            return body;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postForm(String url, Map<String, String> parms) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Set<String> keySet = parms.keySet();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String key : keySet) {
            list.add(new BasicNameValuePair(key, parms.get(key)));
        }
        try {
            HttpPost e = new HttpPost(url);
            e.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            e.setHeader("Content-Type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpclient.execute(e);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            return body;
        } catch (IOException var8) {
            var8.printStackTrace();
            return "{\"errCode\":\"8500\"}";
        }
    }

    /**
     * 只限制卡莱特LED大屏使用，上传文件
     *
     * @param url
     * @param parms
     * @return modify by sunx 20170310
     *//*
    public static String postMultipartForm(String url, JSONObject parms) {
        //设置客户端连接时间
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setStaleConnectionCheckEnabled(true)
                .build();

        //CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        CloseableHttpResponse response = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            //设置请求头
            //e.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            httpPost.setHeader("Content-Type", "multipart/form-data");
            //e.setHeader("User-Agent","SOHUWapRebot");
            httpPost.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
            httpPost.setHeader("Connection", "keep-alive");

            // TODO: 2017/3/10  根据实际情况调整下面数据
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            FileBody fileBody = new FileBody(new File(parms.getString("path")));

            //添加文件，后面设置成多个文件形式
            builder.addBinaryBody("f" + 1, new File("path"));

            //设置字符编码和浏览器兼容模式
            builder.setCharset(Consts.UTF_8).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            HttpEntity reqEntity = builder.build();

            httpPost.setEntity(reqEntity);
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            }

        } catch (IOException var8) {
            logger.error("post工具类请求异常,message：" + var8.getMessage().toString(), var8);
            return "{\"errCode\":\"8500\",\"message\":\"" + var8.getMessage().toString() + "\"}";
        } finally {
            try {
                httpclient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("post工具类请求异常,message：" + e.getMessage().toString(), e);
                return "{\"errCode\":\"8500\",\"message\":\"" + e.getMessage().toString() + "\"}";
            }
        }
        return result;
    }*/

    public static JSONObject postForm2Obj(String url, Map<String, String> parms) {
        try {
            String result = postForm(url, parms);
            return JSON.parseObject(result);
        } catch (Exception var8) {
            var8.printStackTrace();
            return JSON.parseObject("{\"errCode\":\"8500\"}");
        }
    }


    /**
     * 生成固定格式的JSON，卡莱特科技公司专属
     * 硬编码，可能需要换下，成json的
     */
   /* public String paseToJsonOnlyForKTCompany(){
        return "";
    }*/
}
