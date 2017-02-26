package com.buxingzhe.pedestrian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/2/11.
 */

public class MWTStarBar extends LinearLayout {
    private final static int dividerSize = 10;
    private MWTStarOnclick mwtStarOnclick;
    private List<StarBarBean> starBarBeanList;
    private Context mContext;

    public void setStarBarBeanList(List<StarBarBean> starBarBeanList) {
        this.starBarBeanList = starBarBeanList;
        if (mContext != null)
            initView(mContext);
    }
    public MWTStarBar(Context context) {
        super(context);
        initView(context);
    }
    public MWTStarBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MWTStarBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public void initView(Context context){
        mContext = context;
        this.removeAllViews();
        if (starBarBeanList == null){
            starBarBeanList = new ArrayList<StarBarBean>();
            StarBarBean starBarBean = new StarBarBean(R.mipmap.ic_pingzhi_star_yello);
            starBarBeanList.add(starBarBean);
        }
        for (int i=0;i<starBarBeanList.size();i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setId(i);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mwtStarOnclick != null){
                        int id = view.getId()+1;
                        mwtStarOnclick.upStarIco(id);
                    }
                }
            });
            StarBarBean starBarBean = starBarBeanList.get(i);
            LayoutParams params;
            if (starBarBean.height <0 || starBarBean.width < 0){
                 params = new LayoutParams(starBarBean.width,starBarBean.height);
            }else {
                params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            int dividerHeight;
            if (i<starBarBeanList.size() -1){
                if (starBarBean.dividerHeight >0){
                    dividerHeight = starBarBean.dividerHeight;
                }else {
                    dividerHeight = dividerSize;
                }
                params.setMargins(0,0, SystemUtils.dip2px(mContext,dividerHeight),0);
            }else {
                params.setMargins(0,0, 0,0);
            }
            imageView.setLayoutParams(params);
            if (starBarBean.pict <0){
                break;
            }
            imageView.setImageResource(starBarBean.pict);
            addView(imageView);
        }
    }
    public void refreshView(){
        invalidate();
    }
    public void setMwtStarOnclick(MWTStarOnclick mwtStarOnclick) {
        this.mwtStarOnclick = mwtStarOnclick;
        initView(mContext);
    }
}
