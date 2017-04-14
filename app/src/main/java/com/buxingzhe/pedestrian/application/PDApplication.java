package com.buxingzhe.pedestrian.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.buxingzhe.pedestrian.PDConfig;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


/**
 * Created by Administrator on 2016/5/9.
 */
public class PDApplication extends Application {

    {
        // umeng                      appid + appkey
        PlatformConfig.setWeixin("wx609e5d32a2de0351", "3dfae04eac20da17e00a31ff17dc618d");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PDConfig.getInstance().init(this);
        SDKInitializer.initialize(this);

        // umeng
        UMShareAPI.get(this);

    }
}
