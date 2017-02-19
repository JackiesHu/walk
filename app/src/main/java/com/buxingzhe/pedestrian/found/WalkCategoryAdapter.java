package com.buxingzhe.pedestrian.found;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.widget.MWTStarBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/2/11.
 */

public class WalkCategoryAdapter extends RecyclerView.Adapter<WalkCategoryAdapter.WalkCategoryViewHolder>
        {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private Activity mActivity;
    private String[] mTitles;
    private  String[]  datas;
    private View.OnClickListener mOnItemClickListener = null;

    public WalkCategoryAdapter(Context context, Activity mActivity) {
        mContext = context;
        this.mActivity = mActivity;
        mLayoutInflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public WalkCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.walk_category_item, parent, false);
        return new WalkCategoryViewHolder(view);

    }
    @Override
    public void onBindViewHolder(WalkCategoryViewHolder holder, int position) {
        List<StarBarBean> starBarBeens = new ArrayList<>();
        for (int i=0;i<5;i++){
            StarBarBean starBarBean = new StarBarBean(R.mipmap.ic_pingzhi_star_yello);
            starBarBean.dividerHeight = 5;
            starBarBeens.add(starBarBean);
        }
        holder.vStarBar.setStarBarBeanList(starBarBeens);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,WalkDetailsActivity.class);
                mActivity.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return 20;
    }
    public static class WalkCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView vMIvPic;
        TextView vAddressName;
        TextView vWalkNumber;
        TextView vWalkMile;
        TextView vWalkMark;
        MWTStarBar vStarBar;
        WalkCategoryViewHolder(View view) {
            super(view);
            vMIvPic = (ImageView) view.findViewById(R.id.iv_pic);
            vAddressName = (TextView) view.findViewById(R.id.tv_add_name);
            vWalkNumber = (TextView) view.findViewById(R.id.tv_walk_number);
            vWalkMile = (TextView) view.findViewById(R.id.tv_walk_mile);
            vWalkMark = (TextView) view.findViewById(R.id.tv_walk_mark);
            vStarBar = (MWTStarBar) view.findViewById(R.id.tv_walk_star);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}
