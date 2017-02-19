package com.buxingzhe.pedestrian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.SystemUtils;


/**
 * Created by Administrator on 2016/5/10.
 */
public class MWTTabBar extends FrameLayout {
    private LayoutInflater inflater;
    private LinearLayout _containerLayout;
    private TabBarBaseAdapter adapter;

    public void onChangeViewListener(OnClickListener myOnclickListener) {
        this.myOnclickListener = myOnclickListener;
    }

    private OnClickListener myOnclickListener;
    public MWTTabBar(Context context) {
        super(context);
        construct();
    }

    public MWTTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct();
    }

    public MWTTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct();
    }
    private void construct()
    {
         inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        _containerLayout = (LinearLayout) inflater.inflate(R.layout.view_tabbar, this, false);
        addView(_containerLayout);

    }
    public void setAdapter(TabBarBaseAdapter adapter) {
        this.adapter = adapter;
        addChildView();
    }

    private void addChildView() {
        if (adapter != null){
            for (int i=0;i<adapter.getCount();i++){
                View v = adapter.getView(i);
                int[] dis = SystemUtils.getDisplayWidth(getContext());
                int width = dis[0]/adapter.getCount();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                v.setLayoutParams(params);
                v.setId(i);
                if (myOnclickListener != null){
                    v.setOnClickListener(myOnclickListener);
                }
                _containerLayout.addView(v);
            }
        }
    }
}
