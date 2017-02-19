package com.buxingzhe.pedestrian.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.MainActivity;


/**
 * Created by Administrator on 2016/5/10.
 */
public class EnterActUtils {

    public static void enterMainActivity(Activity context){
        Intent mainIntent = new Intent(context,
                MainActivity.class);
        startAct(context, mainIntent);
    }
    /*加入动画效果*/
    public static void startAct(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.enter_login, R.anim.enter_exit_login);
    }
    /*加入动画效果*/
    public static void startFadeAct(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    /*加入动画效果*/
    public static void startScaleAct(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
    }
    public static void startForResultAct(Activity activity,Intent intent,int requestCode){
        activity.startActivityForResult(intent, requestCode);

        activity.overridePendingTransition(R.anim.enter_login,R.anim.enter_exit_login);
    }
    public static void startForResultAct(android.app.Fragment activity,Intent intent,int requestCode){
        activity.startActivityForResult(intent, requestCode);
        activity.getActivity().overridePendingTransition(R.anim.enter_login, R.anim.enter_exit_login);
    }
    public static void startForResultAct(Fragment activity, Intent intent, int requestCode){
        activity.startActivityForResult(intent, requestCode);
        activity.getActivity().overridePendingTransition(R.anim.enter_login, R.anim.enter_exit_login);
    }
    public static void finishActivity(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.exit_entre_login, R.anim.exit_login);
    }
    public static void finishDelayedActivity(Activity activity){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finishActivity(activity);
    }
}
