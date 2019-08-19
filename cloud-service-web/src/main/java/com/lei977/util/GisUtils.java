package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.security.MD5;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by ZENLIN on 2017/3/17.
 */
public class GisUtils {
    private static Logger logger = LoggerFactory.getLogger(GisUtils.class);

    public static RestMessage query(String url, JSONObject jsonObject) {
        String gis_url = ConfigUtils.getTdpServer().getString("gis_url");
        url = gis_url + url;
        RestMessage restMessage = postJsonP(url, jsonObject);
        return restMessage;
    }

    public static RestMessage saveUpdateOrDelete(String url, JSONObject jsonObject) {
        String sign = sign(jsonObject);
        jsonObject.put("sign", sign);
        String gis_url = ConfigUtils.getTdpServer().getString("gis_url");
        url = gis_url + url;
        return postJsonP(url, jsonObject);
    }

    public static String sign(Map<String, Object> params) {
        StringBuffer buffer = new StringBuffer();

        List<String> keys = Lists.newArrayList();
        keys.addAll(params.keySet());

        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            buffer.append(StringUtils.lowerCase((String) keys.get(i))).append("=").append(Objects.toString(params.get(keys.get(i))));
            if (i < keys.size() - 1) {
                buffer.append("&");
            }
        }
        String key = ConfigUtils.getTdpServer().getString("apiKey");
        buffer.append("&key=" + key);
        LogbackLogger.info("md5Str:" + buffer);

        String md5Str = MD5.encrypt(buffer.toString());
        return md5Str;
    }

    public static RestMessage postJsonP(String url, JSONObject jsonObject) {
        RestMessage r = new RestMessage();
        Map<String, String> map = (Map) JSONObject.toJavaObject(jsonObject, Map.class);
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Request Url is null");
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        try {
            if (map != null) {
                List<NameValuePair> params = new ArrayList();
                for (String key : map.keySet()) {
                    params.add(new BasicNameValuePair(key, String.valueOf(map.get(key))));
                }
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            CloseableHttpResponse response = null;
            try {
                response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String cont = EntityUtils.toString(entity, "UTF-8");
                    if (cont.lastIndexOf(")") + 1 == cont.length()) {
                        cont = cont.substring(cont.indexOf("(") + 1, cont.lastIndexOf(")"));
                    }
                    System.out.println(cont.lastIndexOf(")"));
                    System.out.println(cont.length());
                    r = (RestMessage) JSONObject.parseObject(cont, RestMessage.class);
                }
            }
            response.close();
            return r;
        } catch (IOException e) {
            r.setSuccess(false);
            r.setErrorMessage(e.getLocalizedMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                r.setSuccess(false);
                r.setErrorMessage(e.getLocalizedMessage());
            }
        }
        return r;
    }
}
