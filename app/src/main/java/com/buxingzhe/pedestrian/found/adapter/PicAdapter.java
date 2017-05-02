package com.buxingzhe.pedestrian.found.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.PicassManager;

import java.util.List;

/**
 * Created by hasee on 2017/5/2.
 */

public class PicAdapter extends MyBaseAdapter<String> {
    public PicAdapter(Context context, List<String> list, int itemRes) {
        super(context, list, itemRes);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        ImageView iv_pic = holder.getView(R.id.iv_pic);
        PicassManager.getInstance().loadLocal(context,s,iv_pic);
    }
}
