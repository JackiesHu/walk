package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordByActivity;
import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.MWTStarBar;
import com.buxingzhe.pedestrian.widget.TextParser;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CommActInfoAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context mContext;
    private List<WalkRecordByActivity> walkRecordByActivities;

    public CommActInfoAdapter(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (walkRecordByActivities == null) {
            return 0;
        }
        return walkRecordByActivities.size();
    }

    @Override
    public WalkRecordByActivity getItem(int position) {
        return walkRecordByActivities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_comm_act_info, null);
            holder = new Holder();
            holder.cirImag_avatar = (CircularImageView) convertView.findViewById(R.id.cirImag_avatar);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.starBar = (MWTStarBar) convertView.findViewById(R.id.starBar);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_route = (ImageView) convertView.findViewById(R.id.iv_route);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        WalkRecordByActivity walkRecordByActivity = walkRecordByActivities.get(position);
        if (walkRecordByActivity.getUser() != null) {
            UserBaseInfo userBaseInfo = walkRecordByActivity.getUser();
            if (!TextUtils.isEmpty(userBaseInfo.getNickName())) {
                holder.tv_name.setText(userBaseInfo.getNickName());
            }
            if (!TextUtils.isEmpty(userBaseInfo.getAvatarUrl())) {
                Picasso.with(mContext).load(userBaseInfo.getAvatarUrl()).resize(SystemUtils.dip2px(mContext, 40.0f), SystemUtils.dip2px(mContext, 40.0f)).centerCrop().into(holder.cirImag_avatar);
            }
        }
        if (!TextUtils.isEmpty(walkRecordByActivity.getIntroduction())) {
            holder.tv_content.setText(walkRecordByActivity.getIntroduction());
        } else {
            holder.tv_content.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(walkRecordByActivity.getRoutepicStr())) {
            int[] display = SystemUtils.getDisplayWidth(mContext);
            int width = display[0] - SystemUtils.dip2px(mContext, 30);
            Picasso.with(mContext).load(walkRecordByActivity.getRoutepicStr()).resize(width, SystemUtils.dip2px(mContext, 160.0f)).centerCrop().into(holder.iv_route);
        }

        if (!TextUtils.isEmpty(walkRecordByActivity.getCreateTime())) {
            Long t = Long.parseLong(walkRecordByActivity.getCreateTime());
            String date = new java.text.SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date(t * 1000));
            holder.tv_time.setText(date);
        }

        //星级
        List<StarBarBean> starBarBeens = new ArrayList<StarBarBean>();
        StarBarBean starBaarBean;
        int starNum = walkRecordByActivity.getStar();
        for (int i = 0; i < 5; i++) {
            if (i < starNum) {
                starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_yello);
            } else {
                starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
            }
            starBaarBean.height = SystemUtils.dip2px(mContext,12);
            starBaarBean.width = SystemUtils.dip2px(mContext,12);
            starBaarBean.dividerHeight = SystemUtils.dip2px(mContext,6);
            starBarBeens.add(starBaarBean);
        }
        holder.starBar.setStarBarBeanList(starBarBeens);
        return convertView;
    }


    class Holder {
        CircularImageView cirImag_avatar;
        TextView tv_name;
        TextView tv_time;
        MWTStarBar starBar;
        TextView tv_content;
        ImageView iv_route;
    }

    public void setDatas(int page, List<WalkRecordByActivity> walkRecordByActivities) {
        if (this.walkRecordByActivities == null) {
            this.walkRecordByActivities = new ArrayList<WalkRecordByActivity>();
        }
        if (page == 1) {
            this.walkRecordByActivities = walkRecordByActivities;
        } else {
            this.walkRecordByActivities.addAll(walkRecordByActivities);
        }
        notifyDataSetChanged();
    }

//    public boolean areAllItemsEnabled() {
//        return false;
//    }
}
