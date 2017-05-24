package com.buxingzhe.pedestrian.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.widget.TitleBarLinstener;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.umeng.analytics.MobclickAgent;

import rx.Subscription;

/**
 * Created by quanjing on 2017/2/3.
 */
public class BaseActivity extends AppCompatActivity implements TitleBarLinstener {

    protected Subscription mSubscription;
    protected Context mContext;
    protected Activity mActivity;
    protected TitleBarView vTitleBar;
    protected void setTitle(String title){
        if (vTitleBar != null){
            vTitleBar.setvTitle(title);
            onBack();
            initTitle();
        }
    }
    protected void setImgLeftOnclick(View.OnClickListener onclick){
        if (vTitleBar != null){
            vTitleBar.setImgLeftList(onclick);
        }
    }
    protected void setTextRightOnclick(View.OnClickListener onclick){
        if (vTitleBar != null){
            vTitleBar.setTitleBarLinstener(this);
        }
    }
    protected void onBack(){
        setImgLeftOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onLeftTitleListener(View v) {

    }
    @Override
    public void onRightListener(View v) {

    }
    @Override
    public void onRightImageListener(View v) {

    }
    @Override
    public void onLeftImgListener(View v) {

    }
    private void initTitle(){

        if (vTitleBar != null){
            vTitleBar.hideStatusbar();
        }
        if (vTitleBar != null)
            vTitleBar.setTitleBarLinstener(this);
    }
    protected void setRightTitle(String title){
        if (vTitleBar != null){
            vTitleBar.setRight(title);
            vTitleBar.showRight();
        }
    }
    protected void setRightIco(int drawable){
        if (vTitleBar != null){
            vTitleBar.setRightIcon(drawable);
            vTitleBar.showRightImg();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity =this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(null!=GlobalParams.TOKEN){
            if(GlobalParams.TOKEN.length()==0){
                GlobalParams.TOKEN= mContext.getSharedPreferences("token", Context.MODE_PRIVATE).getString("token",null);
                GlobalParams.USER_ID=mContext.getSharedPreferences("userid", Context.MODE_PRIVATE).getString("userid",null);
            }
        }

        String contextString = mContext.toString();
        MobclickAgent.onPageStart(contextString); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        String contextString = mContext.toString();
        MobclickAgent.onPageEnd(contextString); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (mSubscription!=null && mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
