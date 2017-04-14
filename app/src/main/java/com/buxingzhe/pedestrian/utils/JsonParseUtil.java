package com.buxingzhe.pedestrian.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：数据解析工具类
 *
 * @author zhaishaoping
 * @data 09/04/2017 10:30 AM
 */
public class JsonParseUtil {
    private Gson mGson;
    private JsonParseUtil() {
        mGson = new Gson();
    }

    public static JsonParseUtil getInstance() {
        return SingleHolder.sInstance;
    }
    private static class SingleHolder {
        private static final JsonParseUtil sInstance = new JsonParseUtil();
    }


    public <T> Object [] parseJson(String jsonStr, Class<T> clazz) {
        if (TextUtils.isEmpty(jsonStr)){
            return null;
        }
        Object [] dataArray = new Object[3];
        Integer code = -1;

        try {
            JSONObject rootJson = new JSONObject(jsonStr);
            if (rootJson!=null){
                if (rootJson.has("code")){
                    code = rootJson.getInt("code");
                    dataArray[0] = code;
                }

                switch (code){
                    case 0://正常
                        if (rootJson.has("content")){
                            JSONObject content = rootJson.getJSONObject("content");
                            //将 jsonobject 转换成  json
//                            Object bean = mGson.fromJson(mGson.toJson(content), clazz);

                            String toJson = mGson.toJson(content);
//                            Log.i(toJson);//nameValuePairs
                            //String contentJsonString = JSON.toJSONString(content,true);
//                            Log.i(contentJsonString);
                            T bean = JSON.parseObject(toJson.substring("{\"nameValuePairs\":".length(), toJson.length()-1), clazz);
                            dataArray[1] = bean;
                        }
                        break;

                    case 1: //错误提示
                    case 2://token失效
                        if (rootJson.has("content")){
                            String content = rootJson.getString("content");
                            dataArray[2] = content;
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return dataArray;
        }
    }

}
