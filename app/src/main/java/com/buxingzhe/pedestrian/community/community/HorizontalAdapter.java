package com.buxingzhe.pedestrian.community.community;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QJ on 2017/4/13.
 */

public class HorizontalAdapter extends BaseAdapter {
    private Context mContext;
    int currentId = 0;
    List<String> views = new ArrayList<>();

    public HorizontalAdapter(Context mContext,List<String> views) {
        this.mContext = mContext;
        this.views = views;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        HoldeImage holdeImage = null;
        if (view == null){
            holdeImage = new HoldeImage();
            view = View.inflate(mContext, R.layout.item_scroll_linearlayout, null);
            holdeImage.iv_pic = (ImageView) view.findViewById(R.id.index_gallery_item_image);
            view.setTag(holdeImage);
        } else {
            holdeImage = (HoldeImage) view.getTag();
        }
        int[] display = SystemUtils.getDisplayWidth(mContext);
//        int width = (display[0] - SystemUtils.dip2px(mContext, 60)) / 3 - SystemUtils.dip2px(mContext, 10);
        int width = (display[0] - SystemUtils.dip2px(mContext, 42)) / 3;
        int height = width/3*2;

        RelativeLayout.LayoutParams imgvwMargin = new RelativeLayout.LayoutParams(width, height);
        imgvwMargin.setMargins(0, 0, SystemUtils.dip2px(mContext, 6), 0);
        holdeImage.iv_pic.setLayoutParams(imgvwMargin);
        Picasso.with(mContext).load(views.get(position)).resize(width,height).centerCrop().into(holdeImage.iv_pic);
        return view;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
        notifyDataSetChanged();
    }
}

class HoldeImage {
    ImageView iv_pic;
}
