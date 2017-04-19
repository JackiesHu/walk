package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.TextParser;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by quanjing on 2017/4/17.
 */
public class CommActAdapter extends BaseAdapter<WalkActivityInfo> implements View.OnClickListener{
    private Activity mActivity;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;


    public CommActAdapter(Activity activity, Context context) {
        this.mActivity = activity;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public CommActAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_comm_act, null);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(WalkActivityInfo)v.getTag());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WalkActivityInfo walkActivityInfo = getDataSet().get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.itemView.setTag(walkActivityInfo);
        myViewHolder.bind(walkActivityInfo);
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_rl;
        ImageView iv_banner;
        TextView tv_title;
        TextView tv_introduction;
        TextView item_info_TextView;
        TextView tv_attenderCount;
        TextView tv_end_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_banner = (ImageView) itemView.findViewById(R.id.iv_banner);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_introduction = (TextView) itemView.findViewById(R.id.tv_introduction);
            tv_attenderCount = (TextView) itemView.findViewById(R.id.tv_attenderCount);
            tv_end_time = (TextView) itemView.findViewById(R.id.tv_end_time);
            rl_rl = (RelativeLayout) itemView.findViewById(R.id.rl_rl);
        }


        public void bind(WalkActivityInfo walkActivityInfo) {
            int[] display = SystemUtils.getDisplayWidth(mContext);
            int width = display[0] - SystemUtils.dip2px(mContext, 40);
            if (!TextUtils.isEmpty(walkActivityInfo.getBanner())) {
                Picasso.with(mContext).load(walkActivityInfo.getBanner()).resize(width, SystemUtils.dip2px(mContext, 200.0f)).centerCrop().into(iv_banner);
            }
            if (!TextUtils.isEmpty(walkActivityInfo.getTitle())) {
                tv_title.setText(walkActivityInfo.getTitle());
            }
            if (!TextUtils.isEmpty(walkActivityInfo.getIntroduction())) {
                tv_introduction.setText(walkActivityInfo.getIntroduction());
            }
            tv_attenderCount.setText(walkActivityInfo.getAttenderCount() + "人已参加");
            if (!TextUtils.isEmpty(walkActivityInfo.getIsOutDate())){
                //活动是否过期，0 没有 1 有
                if (walkActivityInfo.getIsOutDate().equals("0")){
                    TextParser textParser=new TextParser();
                    textParser.append("倒计时", 13, Color.WHITE);
                    textParser.append(""+residueDate(walkActivityInfo.getEndTimestamp()), 13, mContext.getResources().getColor(R.color.endtime_font));
                    textParser.append("天", 13, Color.WHITE);
                    textParser.parse(tv_end_time);
                }else if (walkActivityInfo.getIsOutDate().equals("1")){
                    tv_end_time.setText(mContext.getResources().getString(R.string.activity_outdate));
                    tv_end_time.setTextColor(mContext.getResources().getColor(R.color.outdate_font));
                }
            }
        }
    }

    /**
     * 倒计时精确到天
     *
     * @param endTimestamp
     * @return
     */
    private long residueDate(long endTimestamp) {
        Date date = new Date();
        Long time = date.getTime();
        long l = endTimestamp - time;
        long days = l / (1000 * 60 * 60 * 24);
        return days;
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , WalkActivityInfo data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
