package com.buxingzhe.pedestrian.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.application.PDApplication;

/**
 * Created by chinaso on 2017/7/17.
 */

public abstract class BasePopupWindow extends PopupWindow {
    public LayoutInflater layoutInflater;

    public BasePopupWindow() {
        layoutInflater = (LayoutInflater) PDApplication.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(getView());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setAnimationStyle(R.style.AnimBottom);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable drawable = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(drawable);
    }

    public abstract View getView();
}