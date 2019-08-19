package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.math.CoordinateConvert;
import com.drore.cloud.sdk.common.math.GoogleMapUtil;
import com.drore.cloud.sdk.domain.map.PointPixel;

import java.math.BigDecimal;

/**
 * Created by ZENLIN on 2017/3/17.
 */
public class GeoUtils {

    private static String MAP_SPACE = "http://121.199.58.84:8082/MapSpace";
    private static String MAP_SPACE_NEW = "http://192.168.11.129:8082/MapSpace";

    /**
     * 计算两经纬度点之间的距离（单位：米）
     *
     * @param lng1 经度
     * @param lat1 纬度
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 计算TP值
     *
     * @param curPoint     当前点
     * @param relatedPoint 偏移点
     * @param isGeography  是否是地理坐标 false为2d坐标
     * @return tp值
     */
    public static double getDirAngle(JSONObject curPoint, JSONObject relatedPoint, boolean isGeography) {
        double result = 0;
        if (isGeography) {
            double y2 = Math.toRadians(relatedPoint.getDouble("lat"));
            double y1 = Math.toRadians(curPoint.getDouble("lat"));
            double alpha = Math.atan2(relatedPoint.getDouble("lat") - curPoint.getDouble("lat"), (relatedPoint.getDouble("lng") - curPoint.getDouble("lng")) * Math.cos((y2 - y1) / 2));//纬度方向乘以cos(y2-y1/2)
            double delta = alpha < 0 ? (2 * Math.PI + alpha) : alpha;
            result = Math.toDegrees(delta);
        } else {
            double alpha = Math.atan2(relatedPoint.getDouble("lat") - curPoint.getDouble("lat"), relatedPoint.getDouble("lng") - curPoint.getDouble("lng"));
            double delta = alpha < 0 ? (2 * Math.PI + alpha) : alpha;
            result = Math.toDegrees(delta);
        }
        return result;
    }

    public static double getDistance() {
        double x1 = 0, y1 = 0, x2 = 10, y2 = 30.5;
        double temp_A, temp_B;
        double c;  // 用来储存算出来的斜边距离
        temp_A = x1 > x2 ? (x1 - x2) : (x2 - x1);  // 横向距离 (取正数，因为边长不能是负数)
        temp_B = y1 > y2 ? (y1 - y2) : (y2 - y1);  // 竖向距离 (取正数，因为边长不能是负数)
        c = Math.sqrt(temp_A * temp_A + temp_B * temp_B);
        return c;
    }



    /**
     * 多点转换
     * 像素转3d坐标
     *
     * @param mapId
     * @param points
     * @return
     */
    public static JSONArray pointToOpLLngLats(String mapId, JSONArray points) {
        if (points.isEmpty()) {
            return points;
        }
        return lngLatTo3DLngLats(mapId, pointToLngLats(mapId, points));
    }

    /**
     * 单点转换
     * 像素转3d坐标
     *
     * @param mapId
     * @param point
     * @return
     */
    public static JSONObject pointToOpLLngLat(String mapId, JSONObject point) {
        if (point.isEmpty()) {
            return point;
        }
        return lngLatTo3DLngLat(mapId, pointToLngLat(mapId, point));
    }

    /**
     * 多点转换
     * 3d坐标转像素
     *
     * @param mapId
     * @param points
     * @return
     */
    public static JSONArray OpLLngLatToPoints(String mapId, JSONArray points) {
        if (points.isEmpty()) {
            return points;
        }
        return lngLatToPoints(mapId, OpLLngLatToLngLats(mapId, points));
    }

    /**
     * 单点转换
     * 3d坐标转像素
     *
     * @param mapId
     * @param point
     * @return
     */
    public static JSONObject OpLLngLatToPoint(String mapId, JSONObject point) {
        if (point.isEmpty()) {
            return point;
        }
        return lngLatToPoint(mapId, OpLLngLatToLngLat(mapId, point));
    }

    /**
     * 经纬度坐标转3d像素坐标 多点
     *
     * @param mapId
     * @param points
     * @return
     */
    public static JSONArray lngLatTo3DLngLats(String mapId, JSONArray points) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "LngLatTo3DLngLat");
        getLngLatTo3DLngLat(converterParms);
        converterParms.put("Values", points);

        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);
        System.out.println(converterParms);
        JSONArray _3dPoints = JSON.parseObject(converterResult).getJSONArray("Result");
        for (int i = 0; i < _3dPoints.size(); i++) {
            JSONObject _3dPoint = _3dPoints.getJSONObject(i);
            //大写X和大写Y转为小写
            _3dPoint.put("x", _3dPoint.remove("X"));
            _3dPoint.put("y", _3dPoint.remove("Y"));
        }
        return _3dPoints;
    }

    public static void main(String[] args) {

    }

    /**
     * 经纬度坐标转3d像素坐标 单点
     *
     * @param mapId
     * @param point
     * @return
     */
    public static JSONObject lngLatTo3DLngLat(String mapId, JSONObject point) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "LngLatTo3DLngLat");
        getLngLatTo3DLngLat(converterParms);
        JSONArray points = new JSONArray();
        points.add(point);
        converterParms.put("Values", points);

        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);
        System.out.println(converterParms);
        JSONObject _3dPoint = JSON.parseObject(converterResult).getJSONArray("Result").getJSONObject(0);
        //大写X和大写Y转为小写
        _3dPoint.put("x", _3dPoint.remove("X"));
        _3dPoint.put("y", _3dPoint.remove("Y"));
        return _3dPoint;
    }

    /**
     * 3d像素坐标转经纬度坐标 多点
     *
     * @param mapId
     * @param points
     * @return
     */
    public static JSONArray OpLLngLatToLngLats(String mapId, JSONArray points) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "3DLngLatToLngLat");
        getOpLLngLatToLngLat(converterParms);
        converterParms.put("Values", points);

        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);
        System.out.println(converterParms);
        JSONArray _3dPoints = JSON.parseObject(converterResult).getJSONArray("Result");
        for (int i = 0; i < _3dPoints.size(); i++) {
            JSONObject _3dPoint = _3dPoints.getJSONObject(i);
            //大写X和大写Y转为小写
            _3dPoint.put("x", _3dPoint.remove("X"));
            _3dPoint.put("y", _3dPoint.remove("Y"));
        }
        return _3dPoints;
    }

    /**
     * 3d像素坐标转经纬度坐标 单点
     *
     * @param mapId
     * @param point
     * @return
     */
    public static JSONObject OpLLngLatToLngLat(String mapId, JSONObject point) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "3DLngLatToLngLat");
        getOpLLngLatToLngLat(converterParms);
        JSONArray points = new JSONArray();
        points.add(point);
        converterParms.put("Values", points);

        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);
        System.out.println(converterParms);
        JSONObject _3dPoint = JSON.parseObject(converterResult).getJSONArray("Result").getJSONObject(0);
        //大写X和大写Y转为小写
        _3dPoint.put("x", _3dPoint.remove("X"));
        _3dPoint.put("y", _3dPoint.remove("Y"));
        return _3dPoint;
    }

    /**
     * 经纬度转像素坐标 多点
     *
     * @param mapId
     * @param points
     * @return
     */
    public static JSONArray lngLatToPoints(String mapId, JSONArray points) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "LngLatToPoint");
        getLngLatToPoint(converterParms);
        converterParms.put("Values", points);

        String converterResult = HttpUtils.postJson(MAP_SPACE_NEW, converterParms);
        System.out.println(converterParms);
        JSONArray pixel_points = JSON.parseObject(converterResult).getJSONArray("Result");
        for (int i = 0; i < pixel_points.size(); i++) {
            JSONObject pixel_point = pixel_points.getJSONObject(i);
            //大写X和大写Y转为小写
            pixel_point.put("x", pixel_point.remove("X"));
            pixel_point.put("y", pixel_point.remove("Y"));
        }
        return pixel_points;
    }

    /**
     * 经纬度转像素坐标 单点
     *
     * @param mapId
     * @param point
     * @return
     */
    public static JSONObject lngLatToPoint(String mapId, JSONObject point) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "LngLatToPoint");
        getLngLatToPoint(converterParms);
        JSONArray points = new JSONArray();
        points.add(point);
        converterParms.put("Values", points);

        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);
        JSONObject pixel_point = JSON.parseObject(converterResult).getJSONArray("Result").getJSONObject(0);
        pixel_point.put("x", pixel_point.remove("X"));
        pixel_point.put("y", pixel_point.remove("Y"));
        return pixel_point;
    }

    /**
     * 像素坐标转经纬度坐标 多点
     *
     * @param mapId
     * @param points
     * @return
     */
    public static JSONArray pointToLngLats(String mapId, JSONArray points) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "PointToLngLat");
        getpointToLngLat(converterParms);
        converterParms.put("Values", points);

        System.out.println(converterParms.toJSONString());
        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);
        JSONArray lngLat_points = JSON.parseObject(converterResult).getJSONArray("Result");
        for (int i = 0; i < lngLat_points.size(); i++) {
            JSONObject lngLat_point = lngLat_points.getJSONObject(i);
            //大写X和大写Y转为小写
            lngLat_point.put("x", lngLat_point.remove("X"));
            lngLat_point.put("y", lngLat_point.remove("Y"));
        }
        return lngLat_points;
    }


    /**
     * 地像素坐标转经纬度坐标 单点
     *
     * @param mapId
     * @param point
     * @return
     */
    public static JSONObject pointToLngLat(String mapId, JSONObject point) {
        JSONObject converterParms = new JSONObject();
        converterParms.put("Action", "PointToLngLat");
        getpointToLngLat(converterParms);
        JSONArray points = new JSONArray();
        points.add(point);
        converterParms.put("Values", points);

        System.out.println(converterParms.toJSONString());
        String converterResult = HttpUtils.postJson(MAP_SPACE, converterParms);

        JSONObject lngLat_point = JSON.parseObject(converterResult).getJSONArray("Result").getJSONObject(0);
        lngLat_point.put("x", lngLat_point.remove("X"));
        lngLat_point.put("y", lngLat_point.remove("Y"));
        return lngLat_point;
    }


    /**
     * 经纬度转3d
     *
     * @param converterParms
     * @return
     */
    private static JSONObject getLngLatTo3DLngLat(JSONObject converterParms) {
        JSONObject point1 = new JSONObject();
        point1.put("X", "120.05595982");
        point1.put("Y", "30.25844246");
        converterParms.put("Point1", point1);
        JSONObject point2 = new JSONObject();
        point2.put("X", "120.08481503");
        point2.put("Y", "30.2672547");
        converterParms.put("Point2", point2);
        JSONObject point3 = new JSONObject();
        point3.put("X", "120.04765034");
        point3.put("Y", "30.28037220");
        converterParms.put("Point3", point3);
        JSONObject pixel1 = new JSONObject();
        pixel1.put("X", "103.60931128");
        pixel1.put("Y", "31.00041306");
        converterParms.put("Pixel1", pixel1);
        JSONObject pixel2 = new JSONObject();
        pixel2.put("X", "103.63891751");
        pixel2.put("Y", "30.99710001");
        converterParms.put("Pixel2", pixel2);
        JSONObject pixel3 = new JSONObject();
        pixel3.put("X", "103.61522287");
        pixel3.put("Y", "31.01450545");
        converterParms.put("Pixel3", pixel3);
        return converterParms;
    }


    /**
     * 经纬度转3d
     *
     * @param converterParms
     * @return
     */
    private static JSONObject getOpLLngLatToLngLat(JSONObject converterParms) {
        JSONObject point1 = new JSONObject();
        point1.put("X", "120.055959821");
        point1.put("Y", "30.25844246");
        converterParms.put("Point1", point1);
        JSONObject point2 = new JSONObject();
        point2.put("X", "120.08481503");
        point2.put("Y", "30.2672547");
        converterParms.put("Point2", point2);
        JSONObject point3 = new JSONObject();
        point3.put("X", "120.04765034");
        point3.put("Y", "30.28037220");
        converterParms.put("Point3", point3);
        JSONObject pixel1 = new JSONObject();
        pixel1.put("X", "103.60931128");
        pixel1.put("Y", "31.00041306");
        converterParms.put("Pixel1", pixel1);
        JSONObject pixel2 = new JSONObject();
        pixel2.put("X", "103.63891751");
        pixel2.put("Y", "30.99710001");
        converterParms.put("Pixel2", pixel2);
        JSONObject pixel3 = new JSONObject();
        pixel3.put("X", "103.61522287");
        pixel3.put("Y", "31.01450545");
        converterParms.put("Pixel3", pixel3);
        return converterParms;
    }


    /**
     * 像素转经纬度
     *
     * @param converterParms
     * @return
     */
    private static JSONObject getpointToLngLat(JSONObject converterParms) {
        JSONObject point1 = new JSONObject();
        point1.put("X", "120.05595982");
        point1.put("Y", "30.25844246");
        converterParms.put("Point1", point1);
        JSONObject point2 = new JSONObject();
        point2.put("X", "120.08481503");
        point2.put("Y", "30.2672547");
        converterParms.put("Point2", point2);
        JSONObject point3 = new JSONObject();
        point3.put("X", "120.04765034");
        point3.put("Y", "30.28037220");
        converterParms.put("Point3", point3);
        JSONObject pixel1 = new JSONObject();
        pixel1.put("X", "9214");
        pixel1.put("Y", "8522");
        converterParms.put("Pixel1", pixel1);
        JSONObject pixel2 = new JSONObject();
        pixel2.put("X", "20253");
        pixel2.put("Y", "9956");
        converterParms.put("Pixel2", pixel2);
        JSONObject pixel3 = new JSONObject();
        pixel3.put("X", "11419");
        pixel3.put("Y", "2384");
        converterParms.put("Pixel3", pixel3);
        return converterParms;
    }


    /**
     * 经纬度转像素
     *
     * @param converterParms
     * @return
     */
    private static JSONObject getLngLatToPoint(JSONObject converterParms) {
        JSONObject point1 = new JSONObject();
        point1.put("X", "9214");
        point1.put("Y", "8522");
        converterParms.put("Point1", point1);
        JSONObject point2 = new JSONObject();
        point2.put("X", "20253");
        point2.put("Y", "9956");
        converterParms.put("Point2", point2);
        JSONObject point3 = new JSONObject();
        point3.put("X", "11419");
        point3.put("Y", "2384");
        converterParms.put("Point3", point3);
        JSONObject pixel1 = new JSONObject();
        pixel1.put("X", "120.05595982");
        pixel1.put("Y", "30.25844246");
        converterParms.put("Pixel1", pixel1);
        JSONObject pixel2 = new JSONObject();
        pixel2.put("X", "120.08481503");
        pixel2.put("Y", "30.2672547");
        converterParms.put("Pixel2", pixel2);
        JSONObject pixel3 = new JSONObject();
        pixel3.put("X", "120.04765034");
        pixel3.put("Y", "30.28037220");
        converterParms.put("Pixel3", pixel3);
        return converterParms;

    }


    /**
     * 通过经纬度获取mapId
     *
     * @return
     */
    public static String getMapIdByPoint(double lng, double lat) {
        boolean isIn = outOfScenicSpot(lat, lng);
        if (isIn) {
            return "e72b9217eb7b4087963dac596dbeeab8";
        } else {
            return "c9404793a2d940e5afd95e5224489d6a";
        }

    }

    public static boolean outOfScenicSpot(double lng, double lat) {
        if (lng < 103.59347820 || lng > 103.62712383)
            return true;
        if (lat < 30.99002838 || lat > 31.01449013)
            return true;
        return false;
    }

    public static JSONArray bd23D(String mapId, JSONArray points) {

        if (mapId.equals("e72b9217eb7b4087963dac596dbeeab8")) {
            //Google基准点坐标x
            BigDecimal scegooglex = new BigDecimal("201.666734695434570");
            BigDecimal scegoogley = new BigDecimal("104.877841949462890");
            BigDecimal spinzoomx = new BigDecimal("0.34983420137891785");
            BigDecimal spinzoomy = new BigDecimal("0.5741219655992548");
            //Google基准点旋转后坐标
            BigDecimal spinx = new BigDecimal("201.726490020751950");
            BigDecimal spiny = new BigDecimal("104.914430618286120");
            byte zoom = 16;
            byte step = 4;
            short yyy = 26846;
            char xxx = 51625;
            for (Object obj : points) {
                JSONObject pointOjb = (JSONObject) obj;
                double[] point = CoordinateConvert.bd092WGS(pointOjb.getDouble("y"), pointOjb.getDouble("x"));
                PointPixel p = GoogleMapUtil.getPixelXY(BigDecimal.valueOf(point[0]), BigDecimal.valueOf(point[1]), scegooglex, scegoogley, spinzoomx, spinzoomy, spinx, spiny, zoom, step, xxx, yyy);
                System.out.println(p.getX() + "," + p.getY());
                ((JSONObject) obj).put("gis_x", p.getX());
                ((JSONObject) obj).put("gis_y", p.getY());
            }
        } else {
            //Google基准点坐标x
            BigDecimal scegooglex = new BigDecimal("201.674274444580080");
            BigDecimal scegoogley = new BigDecimal("104.786645889282240");
            BigDecimal spinzoomx = new BigDecimal("0.3095388570020712");
            BigDecimal spinzoomy = new BigDecimal("0.48604711574434983");
            //Google基准点旋转后坐标
            BigDecimal spinx = new BigDecimal("201.691946029663100");
            BigDecimal spiny = new BigDecimal("104.802417755126940");
            byte zoom = 16;
            byte step = 4;
            short yyy = 26827;
            char xxx = 51629;
            for (Object obj : points) {
                JSONObject pointOjb = (JSONObject) obj;
                double[] point = CoordinateConvert.bd092WGS(pointOjb.getDouble("y"), pointOjb.getDouble("x"));
                PointPixel p = GoogleMapUtil.getPixelXY(BigDecimal.valueOf(point[0]), BigDecimal.valueOf(point[1]), scegooglex, scegoogley, spinzoomx, spinzoomy, spinx, spiny, zoom, step, xxx, yyy);
                ((JSONObject) obj).put("gis_x", p.getX());
                ((JSONObject) obj).put("gis_y", p.getY());
            }

        }

        return points;
    }
}
