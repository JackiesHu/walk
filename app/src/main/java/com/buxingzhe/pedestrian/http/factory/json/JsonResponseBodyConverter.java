package com.buxingzhe.pedestrian.http.factory.json;

import com.buxingzhe.lib.util.Log;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * 类描述：
 *
 * @author zhaishaoping
 * @data 06/04/2017 10:30 PM
 */
public class JsonResponseBodyConverter implements Converter<ResponseBody, RequestResultInfo> {

    public JsonResponseBodyConverter(Gson gson, TypeAdapter adapter) {
        this.adapter = adapter;
        this.gson = gson;
    }

    private final TypeAdapter adapter;
    private final Gson gson;


    @Override
    public RequestResultInfo convert(ResponseBody value) throws IOException {
        // 这里可以处理解密

        try{
            String response = value.string();
            Log.i(response);
            return (RequestResultInfo) adapter.fromJson(response);
        }catch (Exception e){
            // {"code":1,"content":"用户名或密码有误"}
            Log.i(e.getMessage());
            return null;
//            JSONObject jsonObj;
//            try {
//                jsonObj = new JSONObject(value.string());
//                return (RequestResultInfo) jsonObj;
//            } catch(JSONException ee) {
//                Log.i(ee.getMessage());
//                return null;
//            }
        }finally {
            value.close();
        }

        //先将返回的json数据解析到Response中，如果code==200，则解析到我们的实体基类中，否则抛异常
//        Response httpResult = gson.fromJson(response, Response.class);
//        Log.i(httpResult.toString());
//        if (httpResult.code() == 200){
//            //200的时候就直接解析，不可能出现解析异常。因为我们实体基类中传入的泛型，就是数据成功时候的格式
////            return gson.fromJson(response,type);
//            return adapter.fromJson(response);
//        }else {
//            RequestResultInfo<String> errorResponse = gson.fromJson(response,RequestResultInfo.class);
//            //抛一个自定义ResultException 传入失败时候的状态码，和信息
////            throw new RuntimeException("code= "+ errorResponse.getCode() +" ;content= "+ errorResponse.getContent());
//            return (T) errorResponse;
//        }


        //解密字符串
        //return adapter.fromJson(/* EncryptUtils.decode(value.string()) */ value.toString());



        //gson
//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
//        try {
//            return adapter.read(jsonReader);
//        } finally {
//            value.close();
//        }
    }
}
