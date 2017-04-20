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
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsInfo;
import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.HorizontalListView;
import com.buxingzhe.pedestrian.widget.MWTStarBar;
import com.buxingzhe.pedestrian.widget.TextParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by quanjing on 2017/4/13.
 */

public class CommCircleAdapter extends BaseAdapter<WalkRecordInfo> implements View.OnClickListener {
    public Context mContext;
    public LayoutInflater mLayoutInflater;
    private CommCircleAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public CommCircleAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_comm_circle, null);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WalkRecordInfo walkRecordInfo = getDataSet().get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.itemView.setTag(walkRecordInfo);
        myViewHolder.bind(walkRecordInfo);

    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (WalkRecordInfo) view.getTag());
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        CircularImageView cirImag_avatar;
        TextView tv_name;
        TextView tv_time;
        MWTStarBar starBar;
        TextView tv_content;
        HorizontalListView horizontalListView;
        ImageView iv_route;
        TextView tv_location;
        ImageView iv_like;
        RelativeLayout commRL;
        RelativeLayout likeRL;
        TextView tv_comm_count;
        TextView tv_like_count;

        public MyViewHolder(View itemView) {
            super(itemView);
            cirImag_avatar = (CircularImageView) itemView.findViewById(R.id.cirImag_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            starBar = (MWTStarBar) itemView.findViewById(R.id.starBar);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            horizontalListView = (HorizontalListView) itemView.findViewById(R.id.horizontalListView);
            iv_route = (ImageView) itemView.findViewById(R.id.iv_route);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
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
                    PicassManager.getInstance().load(mContext,userBaseInfo.getAvatarUrl(),cirImag_avatar);
                    cirImag_avatar.setBorderWidth(SystemUtils.dip2px(mContext, 1));
                    cirImag_avatar.setBorderColor(R.color.tab_layout_standard);
                }
            }
            if (!TextUtils.isEmpty(walkRecordInfo.getIntroduction())) {
                setContentText(tv_content, walkRecordInfo.getIntroduction());
            } else {
                tv_content.setVisibility(View.GONE);
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
                Picasso.with(mContext).load(walkRecordInfo.getRoutepicStr()).resize(width, SystemUtils.dip2px(mContext, 160.0f)).centerCrop().into(iv_route);
            }
            if (!TextUtils.isEmpty(walkRecordInfo.getLocation())) {
                tv_location.setText(walkRecordInfo.getLocation());
            }
            if (!TextUtils.isEmpty(walkRecordInfo.getCreateTime())) {
                Long t = Long.parseLong(walkRecordInfo.getCreateTime());
                String date = new java.text.SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date(t * 1000));
                tv_time.setText(date);
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
                starBaarBean.height = SystemUtils.dip2px(mContext,12);
                starBaarBean.width = SystemUtils.dip2px(mContext,12);
                starBaarBean.dividerHeight = SystemUtils.dip2px(mContext,6);
                starBarBeens.add(starBaarBean);
            }
            starBar.setStarBarBeanList(starBarBeens);

            //该用户是否已点赞该步行记录,0:未点赞，1：已点赞
            if (walkRecordInfo.getHasLike() == 0) {
                likeRL.setTag(walkRecordInfo);
                likeRL.setOnClickListener(LikeOnClick);
            } else if (walkRecordInfo.getHasLike() == 1) {
                iv_like.setImageResource(R.mipmap.ic_quanzi_zan_pre);
            }
            tv_like_count.setText("" + walkRecordInfo.getLikeCount());
            tv_comm_count.setText("" + walkRecordInfo.getCommentCount());


            commRL.setTag(walkRecordInfo);
            commRL.setOnClickListener(CommentOnClick);
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

    /**
     * 评论
     */
    View.OnClickListener CommentOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WalkRecordInfo walkRecordInfo = (WalkRecordInfo) view.getTag();
            enterWalkRecordCommentActivity();
        }
    };

    /**
     * 点赞
     */
    View.OnClickListener LikeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WalkRecordInfo walkRecordInfo = (WalkRecordInfo) view.getTag();
            walkRecordLike(walkRecordInfo);
        }
    };

    /**
     * 点赞
     */
    public void walkRecordLike(final WalkRecordInfo walkRecordInfo) {
        NetRequestManager.getInstance().walkRecordLike(GlobalParams.USER_ID, GlobalParams.TOKEN, walkRecordInfo.getId(), new Subscriber<String>() {
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
                    walkRecordInfo.setHasLike(1);
                    int likeCount = walkRecordInfo.getLikeCount() + 1;
                    walkRecordInfo.setLikeCount(likeCount);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.activity_like_fail), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void enterWalkRecordCommentActivity() {

    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, WalkRecordInfo data);
    }

    public void setOnItemClickListener(CommCircleAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
