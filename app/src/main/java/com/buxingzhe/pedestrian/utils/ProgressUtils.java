package com.buxingzhe.pedestrian.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.buxingzhe.pedestrian.widget.ProgersssDialog;


/**
 * Created by QJ on 2016/5/18.
 */
public class ProgressUtils {
    static ProgersssDialog progersssDialog;
    static Handler static_handler = null;

    /**
     *
     * @param text
     * @param delay
     * @param isShowProgressbar 是否显示进度条
     */
    public static void showDialog(Context context,String text,float delay, boolean isShowProgressbar){
        if (context == null){
            return;
        }
        if (progersssDialog != null){
            progersssDialog.dismiss();
        }
        progersssDialog = new ProgersssDialog(context);
        static_handler = new Handler();
        static_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
            }
        }, (long) (delay * 1000));
        if (progersssDialog != null){
            progersssDialog.showInView(text, isShowProgressbar);
        }
    }
    public static void showDialog(Context dcontext,String text,int delay){
        showDialog(dcontext, text, delay, false);
    }
    public static void showDialog(Context dcontext,String text,float delay){
        showDialog(dcontext,text,delay,false);
    }
    public static void dismissDialog(){
        if (progersssDialog != null){
            progersssDialog.dismiss();
        }
        if (static_handler != null)
        {
            static_handler.removeCallbacksAndMessages(null);
            static_handler = null;
        }
    }
    public static void onKeyListener(DialogInterface.OnKeyListener onKeyListener){
        if (progersssDialog != null){
            progersssDialog.setOnKeyListener(onKeyListener);
        }

    }
}
