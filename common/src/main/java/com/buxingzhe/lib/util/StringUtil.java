package com.buxingzhe.lib.util;


import java.util.regex.Pattern;

/**
 * Created by zhaishaoping on 31/03/2017.
 */

public class StringUtil {
    /**
     * 手机号码正则表达式
     */
    private static final String PHONE_NUMBER_REGULAR = "^1(3|4|5|7|8)\\d{9}$";

    /**
     * 验证码手机号是否合法
     * @param phoneNumber
     * @return
     */
    public static boolean checkPhone(String phoneNumber) {
        boolean result = false;
        if (isEmpty(phoneNumber)) {
            return result;
        }
        Pattern compile = Pattern.compile(PHONE_NUMBER_REGULAR);
        if (compile != null) {
            result = compile.matcher(phoneNumber).matches();
        }
        return result;
    }

    /**
     * 判断字符串不为 null 或者 长度为0。
     * @param content
     * @return
     */
    public static boolean isEmpty(String content) {
        if (content == null || content.length() == 0) {
            return true;
        }
        return false;
    }
}
