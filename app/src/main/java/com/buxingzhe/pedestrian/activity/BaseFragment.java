package com.buxingzhe.pedestrian.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.buxingzhe.pedestrian.widget.TitleBarLinstener;
import com.buxingzhe.pedestrian.widget.TitleBarView;

/**
 * Created by quanjing on 2017/2/23.
 */
public class BaseFragment extends Fragment implements TitleBarLinstener {
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
        mContext = getContext();
        mActivity = getActivity();
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
}
