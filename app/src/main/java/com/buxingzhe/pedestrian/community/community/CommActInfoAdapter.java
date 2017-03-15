package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.buxingzhe.pedestrian.R;


/**
 * 2
 * Created by qjsj on 2015/12/23.
 */
public class CommActInfoAdapter extends BaseAdapter{
    private Context mContext;
    private Activity mActivity;

    public CommActInfoAdapter(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        if (contentView == null){
            contentView = View.inflate(mContext, R.layout.adapter_commact_info,null);
        }
        return contentView;
    }
}
