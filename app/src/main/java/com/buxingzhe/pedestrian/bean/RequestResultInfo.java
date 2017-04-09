package com.buxingzhe.pedestrian.bean;

/**
 * 请求返回数据的公共接口Bean
 * Created by zhaishaoping on 26/03/2017.
 */

public class RequestResultInfo<T> {
    private String code;//请求状态码  0:正常  1：异常  2：token验证失败
    private T content;//请求结果

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RequestResultInfo{" +
                "code='" + code + '\'' +
                ", content=" + content +
                '}';
    }
}
