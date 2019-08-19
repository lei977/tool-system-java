package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ZENLIN on 2017/3/17.
 */
public class ModelUtil {
    public static JSONArray getModelInfo(Object model)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        JSONArray array = new JSONArray();

        Field[] field = model.getClass().getDeclaredFields();

        String[] modelName = new String[field.length];
        String[] modelType = new String[field.length];
        for (int i = 0; i < field.length; i++)
        {
            JSONObject info = new JSONObject();

            String name = field[i].getName();
            info.put("name", name);
            modelName[i] = name;

            String type = field[i].getGenericType().toString();
            modelType[i] = type;


            field[i].setAccessible(true);
            if (type.equals("class java.lang.String")) {
                info.put("type", "string");
            }
            if (type.equals("class java.lang.Integer")) {
                info.put("type", "integer");
            }
            if (type.equals("class java.lang.Short")) {
                info.put("type", "short");
            }
            if (type.equals("class java.lang.Double")) {
                info.put("type", "double");
            }
            if (type.equals("class java.lang.Boolean")) {
                info.put("type", "boolean");
            }
            if (type.equals("class java.util.Date")) {
                info.put("type", "date");
            }
            Method m = model.getClass().getDeclaredMethod("getFieldName", new Class[] { String.class });
            String value = null;
            try {
                value = (String)m.invoke(model, new Object[] { name });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            info.put("cName", value);
            array.add(info);
        }
        return array;
    }

    public static void main(String[] args)
    {
        try
        {
            System.out.println(getModelInfo(new JSONUtil()));
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
    }
}
