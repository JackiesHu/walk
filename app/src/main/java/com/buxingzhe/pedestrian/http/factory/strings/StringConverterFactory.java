package com.buxingzhe.pedestrian.http.factory.strings;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 类描述：
 *
 * @author zhaishaoping
 * @data 09/04/2017 10:17 AM
 */

public class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create() {
        return create(new Gson());
    }

    public static StringConverterFactory create(Gson gson) {
        return new StringConverterFactory(gson);
    }
    private final Gson gson;

    private StringConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new StringResponseBodyConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestBodyConverter();
    }
}
