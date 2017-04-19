package com.buxingzhe.pedestrian.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 类描述：
 *
 * @author zhaishaoping
 * @data 12/04/2017 10:29 PM
 */
public class SharedPreferencesUtil {

    private SharedPreferencesUtil() {
    }

    public static SharedPreferencesUtil getInstance() {
        return SingleHolder.sInstance;
    }

    private static class SingleHolder {
        private static final SharedPreferencesUtil sInstance = new SharedPreferencesUtil();
    }

    public SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("buxingzhe", Context.MODE_PRIVATE);
    }
}
