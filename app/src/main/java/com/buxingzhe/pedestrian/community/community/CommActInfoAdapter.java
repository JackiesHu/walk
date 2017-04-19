package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordByActivity;
import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by jiangping on 2017/4/19.
 */
public class CommActInfoAdapter extends BaseAdapter<WalkRecordByActivity> implements View.OnClickListener {
    private Activity mActivity;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private CommActInfoAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;


    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    private int mHeaderCount = 1;//头部View个数

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }


    public CommActInfoAdapter(Activity activity, Context context) {
        this.mActivity = activity;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mLayoutInflater.inflate(R.layout.item_comm_act_info, null);
//        view.setOnClickListener(this);
//        return new CommActInfoAdapter.MyViewHolder(view);


        if (viewType == ITEM_TYPE_HEADER) {
            return new CommActInfoAdapter.HeaderViewHolder(mLayoutInflater.inflate(R.layout.activity_info_head, parent, false));
        } else if (viewType == mHeaderCount) {
            return new CommActInfoAdapter.MyViewHolder(mLayoutInflater.inflate(R.layout.item_comm_act_info, parent, false));
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (WalkActivityInfo) v.getTag());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        WalkRecordByActivity walkRecordByActivity = getDataSet().get(position);
//        CommActInfoAdapter.MyViewHolder myViewHolder = (CommActInfoAdapter.MyViewHolder) holder;
//        myViewHolder.itemView.setTag(walkRecordByActivity);
//        myViewHolder.bind(walkRecordByActivity);

        if (holder instanceof CommActInfoAdapter.HeaderViewHolder) {

        } else if (holder instanceof CommActInfoAdapter.MyViewHolder) {
//            ((HeaderBottomAdapter.ContentViewHolder) holder).textView.setText(texts[position - mHeaderCount]);
            WalkRecordByActivity walkRecordByActivity = getDataSet().get(position - mHeaderCount);
            CommActInfoAdapter.MyViewHolder myViewHolder = (CommActInfoAdapter.MyViewHolder) holder;
            myViewHolder.itemView.setTag(walkRecordByActivity);
            myViewHolder.bind(walkRecordByActivity);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircularImageView cirImag_avatar;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_route;

        public MyViewHolder(View itemView) {
            super(itemView);
            cirImag_avatar = (CircularImageView) itemView.findViewById(R.id.cirImag_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            iv_route = (ImageView) itemView.findViewById(R.id.iv_route);
        }

        public void bind(WalkRecordByActivity walkRecordByActivity) {
            if (walkRecordByActivity.getUser() != null) {
                UserBaseInfo userBaseInfo = walkRecordByActivity.getUser();
                if (!TextUtils.isEmpty(userBaseInfo.getNickName())) {
                    tv_name.setText(walkRecordByActivity.getUser().getNickName());
                }
                if (!TextUtils.isEmpty(userBaseInfo.getAvatarUrl())) {
                    Picasso.with(mContext).load(userBaseInfo.getAvatarUrl()).resize(SystemUtils.dip2px(mContext, 40.0f), SystemUtils.dip2px(mContext, 40.0f)).centerCrop().into(cirImag_avatar);
                }
            }
            if (!TextUtils.isEmpty(walkRecordByActivity.getIntroduction())) {
                tv_content.setText(walkRecordByActivity.getIntroduction());
            } else {
                tv_content.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(walkRecordByActivity.getRoutepicStr())) {
                int[] display = SystemUtils.getDisplayWidth(mContext);
                int width = display[0] - SystemUtils.dip2px(mContext, 30);
                Picasso.with(mContext).load(walkRecordByActivity.getRoutepicStr()).resize(width, SystemUtils.dip2px(mContext, 160.0f)).centerCrop().into(iv_route);
            }

            if (!TextUtils.isEmpty(walkRecordByActivity.getCreateTime())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                try {
                    Date date = sdf.parse(walkRecordByActivity.getCreateTime());
                    tv_time.setText(date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private String formatDate(){
//
//    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, WalkActivityInfo data);
    }

    public void setOnItemClickListener(CommActInfoAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
