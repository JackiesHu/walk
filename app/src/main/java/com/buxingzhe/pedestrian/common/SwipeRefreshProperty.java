package com.buxingzhe.pedestrian.common;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;

/**
 * Created by Administrator on 2016/6/15.
 */
public class SwipeRefreshProperty {
    private static  SwipeRefreshProperty install;
    private SwipeRefreshProperty() {
    }
    public static SwipeRefreshProperty getInstall(){
        if (install == null){
            install = new SwipeRefreshProperty();
        }
        return install;
    }
    public void setSwipeInfo(Context context,SwipeRefreshLayout mRefresh){
        //设置刷新时动画的颜色，可以设置4个
        mRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, context.getResources()
                        .getDisplayMetrics()));
    }

}
