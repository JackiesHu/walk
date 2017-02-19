package com.buxingzhe.pedestrian.application;

import android.app.Application;

import com.buxingzhe.pedestrian.PDConfig;


/**
 * Created by Administrator on 2016/5/9.
 */
public class PDApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PDConfig.getInstance().init(this);
    }
}
