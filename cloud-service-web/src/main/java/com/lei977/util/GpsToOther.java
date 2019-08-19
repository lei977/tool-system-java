package com.drore.cloud.tdp.common.util;

import sun.misc.BASE64Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @${author} zhangz
 * @${Date}
 */
public class GpsToOther {
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static final double pi = 3.14159265358979324;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    /**
     * gg_lat 纬度
     * gg_lon 经度
     * GCJ-02转换BD-09
     * Google地图经纬度转百度地图经纬度
     * */
    public static Point google_bd_encrypt(double gg_lat, double gg_lon){
        Point point=new Point();
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        point.setLat(bd_lat);
        point.setLng(bd_lon);
        return point;
    }

    /**
     * wgLat 纬度
     * wgLon 经度
     * BD-09转换GCJ-02
     * 百度转google
     * */
    public static Point bd_google_encrypt(double bd_lat, double bd_lon){
        Point point=new Point();
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta =Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        point.setLat(gg_lat);
        point.setLng(gg_lon);
        return point;
    }



    /**
     * wgLat 纬度
     * wgLon 经度
     * WGS-84 到 GCJ-02 的转换（即 GPS 加偏）
     * */
    public static Point wgs_gcj_encrypts(double wgLat, double wgLon) {
        Point point=new Point();
        if (outOfChina(wgLat, wgLon)) {
            point.setLat(wgLat);
            point.setLng(wgLon);
            return point;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double lat = wgLat + dLat;
        double lon = wgLon + dLon;
        point.setLat(lat);
        point.setLng(lon);
        return point;
    }


    public static void transform(double wgLat, double wgLon, double[] latlng) {
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        latlng[0] = wgLat + dLat;
        latlng[1] = wgLon + dLon;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
    public static void main(String[] args) {
        String x="121.46565145710024"; String y="29.989413814035892";
      //Point p=  bd_google_encrypt(x,y);
        System.out.println(changgeXY(x,y));
    }
    public static String changgeXY(String xx, String yy) {
        try {
            Socket s = new Socket("api.map.baidu.com", 80);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    s.getInputStream(), "UTF-8"));
            OutputStream out = s.getOutputStream();
            StringBuffer sb = new StringBuffer(
                    "GET /ag/coord/convert?from=0&to=4");
            sb.append("&x=" + xx + "&y=" + yy);
            sb.append("&callback=BMap.Convertor.cbk_3976 HTTP/1.1\r\n");
            sb.append("User-Agent: Java/1.6.0_20\r\n");
            sb.append("Host: api.map.baidu.com:80\r\n");
            sb.append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n");
            sb.append("Connection: Close\r\n");
            sb.append("\r\n");
            out.write(sb.toString().getBytes());
            String json = "";
            String tmp = "";
            while ((tmp = br.readLine()) != null) {
                // System.out.println(tmp);
                json += tmp;
            }

            int start = json.indexOf("cbk_3976");
            int end = json.lastIndexOf("}");
            if (start != -1 && end != -1&& json.contains("\"x\":\"")) {
                json = json.substring(start, end);
                String[] point = json.split(",");
                String x = point[1].split(":")[1].replace("\"", "");
                String y = point[2].split(":")[1].replace("\"", "");
                return (new String(decode(x)) + "," + new String(decode(y)));
            } else {
                System.out.println("gps坐标无效！！");
            }
            out.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解码
     *
     * @param str
     * @return string
     */
    public static byte[] decode(String str) {

        byte[] bt = null;

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            bt = decoder.decodeBuffer(str);
            // System.out.println(new String (bt));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bt;
    }
}
