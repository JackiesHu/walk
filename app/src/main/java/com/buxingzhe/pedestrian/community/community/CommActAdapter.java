package com.buxingzhe.pedestrian.community.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.bean.AdvCommunityData;
import com.buxingzhe.pedestrian.utils.EnterActUtils;

import java.util.ArrayList;

/**
 * Created by quanjing on 2017/2/28.
 */
public class CommActAdapter extends RecyclerView.Adapter<CommActAdapter.CommActHolder>
{
    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<AdvCommunityData> datas = new ArrayList<AdvCommunityData>();
    public CommActAdapter(Context context,Activity mActivity,ArrayList<AdvCommunityData> datas) {
        this.datas = datas;
        this.mContext = context;
        this.mActivity = mActivity;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public CommActAdapter.CommActHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.activityitem_nowac_item,null);
        view.setOnClickListener(new myCommAct());
        return new CommActHolder(view);
    }

    @Override
    public void onBindViewHolder(CommActAdapter.CommActHolder holder, int position) {
        //holder.itemView.setTag(datas.get(position));
        /*if(datas!=null && datas.size()>0){
            final AdvCommunityData listdata=datas.get(position);
            if(listdata!=null){
                //Picasso.with(mContext).load(listdata.logoUrl).into(holder.nowitem_Imageview);
                TextParser pictexParser=new TextParser();
                pictexParser.parse(holder.tv_nv_time);
                TextParser pictextParser=new TextParser();
                pictextParser.append("已参赛 ", 12, Color.WHITE);
                pictextParser.append(listdata.imageCount+"", 12, Color.parseColor("#F24E16"));
                pictextParser.parse(holder.tv_nv_num);

                holder.item_info_TextView.setText(listdata.summary);
                holder.nowitem_Imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击流统计
                        if (listdata.type == 1) {

                        } else {

                        }
                    }
                });
            }
        }else{
            holder.rl_rl.setVisibility(View.GONE);
        }*/
    }
    class myCommAct implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(mContext,CommActInfoActivity.class);
            EnterActUtils.startAct(mActivity,intent);
        }
    }
    @Override
    public int getItemCount() {
        if (datas == null)
            return 0;
        else
            return 5;
    }
    public static class CommActHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_rl;
        ImageView nowitem_Imageview;
        TextView item_Time_TextView;
        TextView item_num_TextView;
        TextView item_info_TextView;
        TextView tv_nv_num;
        TextView tv_nv_time;

        public CommActHolder(View itemView) {
            super(itemView);
            nowitem_Imageview= (ImageView) itemView.findViewById(R.id.nowitem_Imageview);
            item_Time_TextView = (TextView) itemView.findViewById(R.id.item_Time_TextView);
            item_num_TextView= (TextView) itemView.findViewById(R.id.item_num_TextView);
            tv_nv_num= (TextView) itemView.findViewById(R.id.tv_nv_num);
            tv_nv_time= (TextView) itemView.findViewById(R.id.tv_nv_time);
            rl_rl= (RelativeLayout) itemView.findViewById(R.id.rl_rl);
        }
    }
}
