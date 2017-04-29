package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.TextParser;
import com.buxingzhe.pedestrian.widget.XCRoundRectImageView;
import com.pizidea.imagepicker.bean.ImageItem;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.buxingzhe.pedestrian.R;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CommActAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context mContext;
    private List<WalkActivityInfo> walkActivityInfos;

    public CommActAdapter(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (walkActivityInfos == null) {
            return 0;
        }
        return walkActivityInfos.size();
    }

    @Override
    public WalkActivityInfo getItem(int position) {
        return walkActivityInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_comm_act, null);
            holder = new Holder();
            holder.iv_banner = (XCRoundRectImageView) convertView.findViewById(R.id.iv_banner);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_introduction = (TextView) convertView.findViewById(R.id.tv_introduction);
            holder.tv_attenderCount = (TextView) convertView.findViewById(R.id.tv_attenderCount);
            holder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        WalkActivityInfo walkActivityInfo = walkActivityInfos.get(position);
        int[] display = SystemUtils.getDisplayWidth(mContext);
        int width = display[0] - SystemUtils.dip2px(mContext, 40);
        if (!TextUtils.isEmpty(walkActivityInfo.getBanner())) {
            Picasso.with(mContext).load(walkActivityInfo.getBanner()).resize(width, SystemUtils.dip2px(mContext, 200.0f)).centerCrop().into(holder.iv_banner);
        }else{
            holder.iv_banner.setImageResource(R.mipmap.ic_shequ_tupian_moren);
        }
        if (!TextUtils.isEmpty(walkActivityInfo.getTitle())) {
            holder.tv_title.setText(walkActivityInfo.getTitle());
        }
        if (!TextUtils.isEmpty(walkActivityInfo.getIntroduction())) {
            holder.tv_introduction.setText(walkActivityInfo.getIntroduction());
        }
        holder.tv_attenderCount.setText(walkActivityInfo.getAttenderCount() + "人已参加");
        if (!TextUtils.isEmpty(walkActivityInfo.getIsOutDate())){
            //活动是否过期，0 没有 1 有
            if (walkActivityInfo.getIsOutDate().equals("0")){
                TextParser textParser=new TextParser();
                textParser.append("倒计时", 13, Color.WHITE);
                textParser.append(""+residueDate(walkActivityInfo.getEndTimestamp()), 13, mContext.getResources().getColor(R.color.endtime_font));
                textParser.append("天", 13, Color.WHITE);
                textParser.parse(holder.tv_end_time);
            }else if (walkActivityInfo.getIsOutDate().equals("1")){
                holder.tv_end_time.setText(mContext.getResources().getString(R.string.activity_outdate));
                holder.tv_end_time.setTextColor(mContext.getResources().getColor(R.color.outdate_font));
            }
        }

        holder.iv_banner.setTag(walkActivityInfo);
        holder.iv_banner.setOnClickListener(EnterOnClick);
        return convertView;
    }


    class Holder {
        XCRoundRectImageView iv_banner;
        TextView tv_title;
        TextView tv_introduction;
        TextView tv_attenderCount;
        TextView tv_end_time;
    }

    public void setWalkActivityInfos(int page, List<WalkActivityInfo> walkActivityInfos) {
        if (this.walkActivityInfos == null) {
            this.walkActivityInfos = new ArrayList<WalkActivityInfo>();
        }
        if (page == 1) {
            this.walkActivityInfos = walkActivityInfos;
        } else {
            this.walkActivityInfos.addAll(walkActivityInfos);
        }
        notifyDataSetChanged();
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

    View.OnClickListener EnterOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WalkActivityInfo walkActivityInfo = (WalkActivityInfo) v.getTag();
            enterCommActInfoActivity(walkActivityInfo);
        }
    };


    private void enterCommActInfoActivity(WalkActivityInfo walkActivityInfo) {
        Intent intent = new Intent();
        intent.setClass(mContext, CommActInfoActivity.class);
        intent.putExtra(CommActFragment.WALKACTIVITYINFO,walkActivityInfo);
        EnterActUtils.startAct(mActivity, intent);
    }
}
