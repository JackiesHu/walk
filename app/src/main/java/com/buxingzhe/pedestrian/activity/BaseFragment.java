package com.buxingzhe.pedestrian.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.buxingzhe.pedestrian.widget.TitleBarLinstener;
import com.buxingzhe.pedestrian.widget.TitleBarView;

import rx.Subscription;

/**
 * Created by quanjing on 2017/2/23.
 */
public class BaseFragment extends Fragment implements TitleBarLinstener {
    protected Subscription mSubscription;

    protected Context mContext;
    protected Activity mActivity;
    protected TitleBarView vTitleBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
    }

    protected void setTitle(String title){
        if (vTitleBar != null){
            vTitleBar.setvTitle(title);
//            onBack();
            initTitle();
        }
    }

    protected void setTitleRight(String titleRight){
        if (vTitleBar != null){
            vTitleBar.setRight(titleRight);
            initTitle();
        }
    }
    protected void setTitleLeft(String titleLeft){
        if (vTitleBar != null){
            vTitleBar.setLeft(titleLeft);
            initTitle();
        }
    }
    protected void setTitleLeftOnclick(){
        if (vTitleBar != null){
            vTitleBar.setLeftList();
        }
    }
    protected void setTitleRightOnclick(){
        if (vTitleBar != null){
            vTitleBar.setRightList();
        }
    }

    protected void setImgLeftOnclick(View.OnClickListener onclick){
        if (vTitleBar != null){
            vTitleBar.setImgLeftList(onclick);
        }
    }
    protected void setImgLeftOnclick(){
        if (vTitleBar != null){
            vTitleBar.setImgLeftList();
        }
    }
    protected void setImgRightOnclick(View.OnClickListener onclick){
        if (vTitleBar != null){
            vTitleBar.setImgRightList(onclick);
        }
    }
    protected void setImgRightOnclick(){
        if (vTitleBar != null){
            vTitleBar.setImgRightList();
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
                getActivity().finish();
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
    }
    protected void hideLeftIco(){
        if (vTitleBar != null){
            vTitleBar.hideLeft();
        }
    }
    protected void showLeftIco(){
        if (vTitleBar != null){
            vTitleBar.showLeftImag();
        }
    }
    protected void hideRightIco(){
        if (vTitleBar != null){
            vTitleBar.hideRightImg();
        }
    }
    protected void showRightIco(){
        if (vTitleBar != null){
            vTitleBar.showRightImg();
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscription!=null && mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
