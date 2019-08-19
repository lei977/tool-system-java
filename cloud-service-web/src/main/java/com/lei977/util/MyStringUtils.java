package com.drore.cloud.tdp.common.util;

/**
 * 卓锐科技有限公司
 * Created by wmm on 2016/9/2.
 * email：6492178@gmail.com
 * description:
 */
public class MyStringUtils {

    public static boolean isNotEmpty(Object... strings) {
        for (Object string : strings) {
            if (string == null || "".equals(string)||"null".equals(string)) {
                return false;
            }
        }
        return true;
    }
}
