package com.buxingzhe.pedestrian.http;

import com.buxingzhe.pedestrian.bean.RequestResultInfo;

/**
 * 请求数据结果回调接口
 * Created by zhaishaoping on 27/03/2017.
 */

public interface NetRequestResultHandler<T> {
    void onSuccess(RequestResultInfo<T> resultInfo);
    void onFailed(String msg);
}
