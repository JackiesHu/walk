package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsInfo;
import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.HorizontalListView;
import com.buxingzhe.pedestrian.widget.MWTStarBar;
import com.buxingzhe.pedestrian.widget.TextParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CommCircleAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context mContext;
    private List<WalkRecordInfo> walkRecordInfos;
    private PDApplication mApp;
    public CommCircleAdapter(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        mApp = PDApplication.getApp();
    }

    @Override
    public int getCount() {
        if (walkRecordInfos == null) {
            return 0;
        }
        return walkRecordInfos.size();
    }

    @Override
    public WalkRecordInfo getItem(int position) {
        return walkRecordInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_comm_circle, null);
            holder = new Holder();
            holder.cirImag_avatar = (CircularImageView) convertView.findViewById(R.id.cirImag_avatar);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.starBar = (MWTStarBar) convertView.findViewById(R.id.starBar);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
//            holder.horizontalListView = (HorizontalListView) convertView.findViewById(R.id.horizontalListView);
            holder.viewsLL = (LinearLayout) convertView.findViewById(R.id.viewsLL);
            holder.iv_route = (ImageView) convertView.findViewById(R.id.iv_route);
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
            holder.commRL = (RelativeLayout) convertView.findViewById(R.id.commRL);
            holder.likeRL = (RelativeLayout) convertView.findViewById(R.id.likeRL);
            holder.tv_comm_count = (TextView) convertView.findViewById(R.id.tv_comm_count);
            holder.tv_like_count = (TextView) convertView.findViewById(R.id.tv_like_count);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        int[] display = SystemUtils.getDisplayWidth(mContext);
        WalkRecordInfo walkRecordInfo = walkRecordInfos.get(position);
        if (walkRecordInfo.getUser() != null) {
            UserBaseInfo userBaseInfo = walkRecordInfo.getUser();
            if (!TextUtils.isEmpty(userBaseInfo.getNickName())) {
                holder.tv_name.setText(walkRecordInfo.getUser().getNickName());
            }
            if (!TextUtils.isEmpty(userBaseInfo.getAvatarUrl())) {
                PicassManager.getInstance().load(mContext, userBaseInfo.getAvatarUrl(), holder.cirImag_avatar);
                holder.cirImag_avatar.setBorderWidth(SystemUtils.dip2px(mContext, 1));
                holder.cirImag_avatar.setBorderColor(R.color.tab_layout_standard);
            }
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getIntroduction())) {
            setContentText(holder.tv_content, walkRecordInfo.getIntroduction());
        } else {
            holder.tv_content.setVisibility(View.GONE);
        }
        holder.viewsLL.removeAllViews();
        if (!TextUtils.isEmpty(walkRecordInfo.getViews())) {
            String[] strings = walkRecordInfo.getViews().split(";");
//            List<String> views = new ArrayList<>();
            int width = (display[0] - SystemUtils.dip2px(mContext, 42)) / 3;
            int height = width / 3 * 2;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.setMargins(0, 0, SystemUtils.dip2px(mContext, 6), 0);
            for (int i = 0; i < (strings.length>3?2:strings.length); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(params);
                Picasso.with(mContext).load(strings[i]).resize(width,height).centerCrop().into(imageView);
                holder.viewsLL.addView(imageView);
//                views.add(strings[i]);
            }
//            holder.horizontalListView.setAdapter(new HorizontalAdapter(mContext, views));


        }

        if (!TextUtils.isEmpty(walkRecordInfo.getRoutepicStr())) {
            int width = display[0] - SystemUtils.dip2px(mContext, 30);
            Picasso.with(mContext).load(walkRecordInfo.getRoutepicStr()).resize(width, SystemUtils.dip2px(mContext, 160.0f)).centerCrop().into(holder.iv_route);
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getLocation())) {
            holder.tv_location.setText(walkRecordInfo.getLocation());
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getCreateTime())) {
            Long t = Long.parseLong(walkRecordInfo.getCreateTime());
            String date = new java.text.SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date(t * 1000));
            holder.tv_time.setText(date);
        }
        //星级
        List<StarBarBean> starBarBeens = new ArrayList<StarBarBean>();
        StarBarBean starBaarBean;
        int starNum = walkRecordInfo.getStar();
        for (int i = 0; i < 5; i++) {
            if (i < starNum) {
                starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_yello);
            } else {
                starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
            }
            starBaarBean.height = SystemUtils.dip2px(mContext, 12);
            starBaarBean.width = SystemUtils.dip2px(mContext, 12);
            starBaarBean.dividerHeight = SystemUtils.dip2px(mContext, 6);
            starBarBeens.add(starBaarBean);
        }
        holder.starBar.setStarBarBeanList(starBarBeens);

        //该用户是否已点赞该步行记录,0:未点赞，1：已点赞
        if (walkRecordInfo.getHasLike() == 0) {
            holder.iv_like.setImageResource(R.mipmap.ic_quanzi_zan);
        } else if (walkRecordInfo.getHasLike() == 1) {
            holder.iv_like.setImageResource(R.mipmap.ic_quanzi_zan_pre);
        }
        holder.tv_like_count.setText("" + walkRecordInfo.getLikeCount());
        holder.tv_comm_count.setText("" + walkRecordInfo.getCommentCount());


        holder.likeRL.setId(position);
        holder.likeRL.setTag(holder);
        holder.likeRL.setOnClickListener(LikeOnClick);

        holder.commRL.setTag(walkRecordInfo);
        holder.commRL.setOnClickListener(CommentOnClick);
        return convertView;
    }


    class Holder {
        CircularImageView cirImag_avatar;
        TextView tv_name;
        TextView tv_time;
        MWTStarBar starBar;
        TextView tv_content;
        HorizontalListView horizontalListView;
        LinearLayout viewsLL;
        ImageView iv_route;
        TextView tv_location;
        ImageView iv_like;
        RelativeLayout commRL;
        RelativeLayout likeRL;
        TextView tv_comm_count;
        TextView tv_like_count;

    }

    public void setDatas(int page, List<WalkRecordInfo> walkRecordInfos) {
        if (this.walkRecordInfos == null) {
            this.walkRecordInfos = new ArrayList<WalkRecordInfo>();
        }
        if (page == 1) {
            this.walkRecordInfos = walkRecordInfos;
        } else {
            this.walkRecordInfos.addAll(walkRecordInfos);
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


    /**
     * 评论
     */
    View.OnClickListener CommentOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WalkRecordInfo walkRecordInfo = (WalkRecordInfo) view.getTag();
            enterWalkRecordCommentActivity(walkRecordInfo);
        }
    };

    /**
     * 点赞
     */
    View.OnClickListener LikeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Holder holder = (Holder) view.getTag();
            int position = view.getId();
            walkRecordLike(position,holder);
        }
    };

    /**
     * 点赞
     */
    public void walkRecordLike(final int position, final Holder holder) {
        //该用户是否已点赞该步行记录,0:未点赞，1：已点赞
        if (walkRecordInfos.get(position).getHasLike()==1){
            return;
        }
        NetRequestManager.getInstance().walkRecordLike(mApp.getUserId(), mApp.getUserToken(), walkRecordInfos.get(position).getId(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext, mContext.getString(R.string.activity_like_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkRecordsInfo.class);
                if ((Integer) datas[0] == 0) {
                    //判断点赞成功之后，手动更改ui
                    walkRecordInfos.get(position).setHasLike(1);
                    int likeCount = walkRecordInfos.get(position).getLikeCount() + 1;
                    walkRecordInfos.get(position).setLikeCount(likeCount);

                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.activity_like_fail), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void enterWalkRecordCommentActivity(WalkRecordInfo walkRecordInfo) {
        Intent intent = new Intent(mContext,CommCircleCommentActivity.class);
        intent.putExtra(CommCircleCommentActivity.WALKRECORDINFO,walkRecordInfo);
        EnterActUtils.startAct(mActivity,intent);
    }

}
