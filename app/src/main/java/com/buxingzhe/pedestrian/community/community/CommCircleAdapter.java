package com.buxingzhe.pedestrian.community.community;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.utils.TextParser;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.HorizontalListView;

/**
 * Created by quanjing on 2017/4/13.
 */

//extends RecyclerView.Adapter<CommCircleAdapter.CommActHolder>
public class CommCircleAdapter extends BaseAdapter<String>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
//    private List<WalkRecordInfo> walkRecordInfos = new ArrayList<>();

    public CommCircleAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

//    public CommCircleAdapter(Context context, List<WalkRecordInfo> walkRecordInfos) {
//        this.mContext = context;
//        mLayoutInflater = LayoutInflater.from(mContext);
//        this.walkRecordInfos = walkRecordInfos;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_comm_circle, null);
        view.setOnClickListener(new myCommAct());
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String str = getDataSet().get(position);
        ((MyViewHolder) holder).bind(str);
    }


//    @Override
//    public void onBindViewHolder(CommCircleAdapter.MyViewHolder holder, int position) {
//        holder.horizontalListView.setAdapter(new HorizontalAdapter(mContext));
//        setContentText(holder.tv_content, "夜景也太美丽了！美食也好好吃!夜景实在是太美了！美食也好好吃夜景也太美丽了！美食也好好吃!夜景实在是太美了！美食也好好吃夜景也太美丽了！美食也好好吃!夜景实在是太美了！美食也好好吃\"\n");
//    }

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


    static class MyViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(CharSequence content) {
            tv_name.setText(content);
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


//    public void setWalkRecordInfos(int index, List<WalkRecordInfo> walkRecordInfos) {
//        if (index == 1) {
//            this.walkRecordInfos.clear();
//        }
//        this.walkRecordInfos.addAll(walkRecordInfos);
//        notifyDataSetChanged();
//    }
}
