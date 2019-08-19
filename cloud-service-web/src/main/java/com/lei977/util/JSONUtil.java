package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZENLIN on 2017/3/17.
 */
public class JSONUtil {
    public static JSONObject goson2FastjsonUtil(JsonObject object) {
        return JSON.parseObject(object.toString());
    }

    public static JSONObject obj2JSONObject(Object obj) {
        return JSON.parseObject(JSON.toJSONString(obj));
    }

    /**
     * add by sunx 20170619
     * @param element
     * @return
     */
    public static JSONObject xml2JSONObject(Element element) {
        try {
            Map map = iterateElement(element);
            JSONObject jsonObject = (JSONObject) JSON.toJSON(map);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 有点耗时。。。 数据量大时不建议用
     * @param root
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map iterateElement(Element root) {
        if(root == null) return new HashMap<>();

        List childrenList = root.elements();
        Element element = null;
        Map map = new HashMap();
        List list = null;
        for (int i = 0; i < childrenList.size(); i++) {
            list = new ArrayList();
            element = (Element) childrenList.get(i);
            if (element.elements().size() > 0) {
                if (root.elements(element.getName()).size() > 1) {
                    if (map.containsKey(element.getName())) {
                        list = (List) map.get(element.getName());
                    }
                    list.add(iterateElement(element));
                    map.put(element.getName(), list);
                } else {
                    map.put(element.getName(), iterateElement(element));
                }
            } else {
                if (root.elements(element.getName()).size() > 1) {
                    if (map.containsKey(element.getName())) {
                        list = (List) map.get(element.getName());
                    }
                    list.add(element.getTextTrim());
                    map.put(element.getName(), list);
                } else {
                    map.put(element.getName(), element.getTextTrim());
                }
            }
        }

        return map;
    }
}
