package com.buxingzhe.pedestrian.http.factory.strings;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 类描述：
 *
 * @author zhaishaoping
 * @data 09/04/2017 10:19 AM
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
