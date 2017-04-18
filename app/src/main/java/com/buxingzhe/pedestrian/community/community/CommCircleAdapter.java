package com.buxingzhe.pedestrian.community.community;

import android.content.Context;
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
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.TextParser;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.HorizontalListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quanjing on 2017/4/13.
 */

public class CommCircleAdapter extends BaseAdapter<WalkRecordInfo> {
    public Context mContext;
    public LayoutInflater mLayoutInflater;

    public CommCircleAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_comm_circle, null);
        view.setOnClickListener(new myCommAct());
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WalkRecordInfo walkRecordInfo = getDataSet().get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.bind(walkRecordInfo);

    }


    class myCommAct implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            Intent intent = new Intent();
//            intent.setClass(mContext,CommActInfoActivity.class);
//            EnterActUtils.startAct(mActivity,intent);
        }
    }

//    @Override
//    public int getItemCount() {
//        if (walkRecordInfos == null) {
//            return 0;
//        } else {
//            return walkRecordInfos.size();
//        }
//    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        CircularImageView cirImag_avatar;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        HorizontalListView horizontalListView;
        ImageView iv_route;
        TextView tv_location;
        RelativeLayout commRL;
        RelativeLayout likeRL;
        TextView tv_comm_count;
        TextView tv_like_count;

        public MyViewHolder(View itemView) {
            super(itemView);
            cirImag_avatar = (CircularImageView) itemView.findViewById(R.id.cirImag_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            horizontalListView = (HorizontalListView) itemView.findViewById(R.id.horizontalListView);
            iv_route = (ImageView) itemView.findViewById(R.id.iv_route);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            commRL = (RelativeLayout) itemView.findViewById(R.id.commRL);
            likeRL = (RelativeLayout) itemView.findViewById(R.id.likeRL);
            tv_comm_count = (TextView) itemView.findViewById(R.id.tv_comm_count);
            tv_like_count = (TextView) itemView.findViewById(R.id.tv_like_count);
        }

        public void bind(WalkRecordInfo walkRecordInfo) {
            if (walkRecordInfo.getUser() != null) {
                UserBaseInfo userBaseInfo = walkRecordInfo.getUser();
                if (!TextUtils.isEmpty(userBaseInfo.getNickName())) {
                    tv_name.setText(walkRecordInfo.getUser().getNickName());
                }
                if (!TextUtils.isEmpty(userBaseInfo.getAvatarUrl())) {
                    Picasso.with(mContext).load(userBaseInfo.getAvatarUrl()).resize(SystemUtils.dip2px(mContext, 40.0f), SystemUtils.dip2px(mContext, 40.0f)).centerCrop().into(cirImag_avatar);
                }
            }
            if (!TextUtils.isEmpty(walkRecordInfo.getIntroduction())) {
                setContentText(tv_content, walkRecordInfo.getIntroduction());
            }
            if (!TextUtils.isEmpty(walkRecordInfo.getViews())) {
                String[] strings = walkRecordInfo.getViews().split(";");
                List<String> views = new ArrayList<>();
                for (int i = 0; i < strings.length; i++) {
                    views.add(strings[i]);
                }
                horizontalListView.setAdapter(new HorizontalAdapter(mContext, views));
            }

            if (!TextUtils.isEmpty(walkRecordInfo.getRoutepicStr())) {
                int[] display = SystemUtils.getDisplayWidth(mContext);
                int width = display[0] - SystemUtils.dip2px(mContext, 30);
                Picasso.with(mContext).load(walkRecordInfo.getRoutepicStr()).resize(width,SystemUtils.dip2px(mContext, 160.0f)).centerCrop().into(iv_route);
            }
            if (!TextUtils.isEmpty(walkRecordInfo.getLocation())) {
                tv_location.setText(walkRecordInfo.getLocation());
            }
            tv_time.setText("" + walkRecordInfo.getCreateTime());
            tv_like_count.setText("" + walkRecordInfo.getLikeCount());
            tv_comm_count.setText("" + walkRecordInfo.getCommentCount());

        }
    }

    /**
     * 只显示三行，超过三行显示“全文”按钮
     *
     * @param tv_content
     * @param str
     */
    private void setContentText(TextView tv_content, String str) {
        String newstr = "";
        int scWidth = SystemUtils.getDisplayWidth(mContext)[0];
        int tvWidth = scWidth - SystemUtils.dip2px(mContext, 30);
        int textsize = SystemUtils.sp2px(mContext, 14);
        int linecount = tvWidth / textsize;

        if (str.length() > linecount * 3) {
            newstr = str.substring(0, linecount * 3);
            TextParser liketextParser = new TextParser();
            liketextParser.append(newstr, 14, mContext.getResources().getColor(R.color.grey_font));
            liketextParser.append("... \n", 14, mContext.getResources().getColor(R.color.grey_font));
            liketextParser.append("全文", 14, mContext.getResources().getColor(R.color.blue_font));
            liketextParser.parse(tv_content);
        } else {
            newstr = str;
            tv_content.setText(newstr);
        }
    }

}
