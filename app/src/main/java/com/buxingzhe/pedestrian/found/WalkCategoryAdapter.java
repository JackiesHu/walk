package com.buxingzhe.pedestrian.found;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.adapter.RecycleBaseAdapter;
import com.buxingzhe.pedestrian.found.adapter.RecycleViewHolder;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.bean.WalkRecord;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.widget.MWTStarBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/2/11.
 */

public class WalkCategoryAdapter extends RecycleBaseAdapter<WalkRecord> {

    public WalkCategoryAdapter(Context context, List<WalkRecord> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public void convert(RecycleViewHolder holder, WalkRecord walkRecord, int position) {
        MWTStarBar vStarBar = holder.getView(R.id.tv_walk_star);
        ImageView iv_pic = holder.getView(R.id.iv_pic);
        TextView tv_add_name = holder.getView(R.id.tv_add_name);
        TextView tv_walk_number = holder.getView(R.id.tv_walk_number);
        TextView tv_walk_mile = holder.getView(R.id.tv_walk_mile);
        TextView tv_walk_mark = holder.getView(R.id.tv_walk_mark);

        tv_walk_number.setText("步行数："+ walkRecord.getStepCount() + "步");
        tv_walk_mile.setText("里程："+ walkRecord.getDistance() + "m");
        tv_walk_mark.setText(walkRecord.getEnvirStar() + "分");
        PicassManager.getInstance().load(context,walkRecord.getUser().getAvatarUrl(),iv_pic);
        tv_add_name.setText(walkRecord.getTitle());
        List<StarBarBean> starBarBeens = new ArrayList<>();
        for (int i=0;i<walkRecord.getEnvirStar();i++){
            StarBarBean starBarBean = new StarBarBean(R.mipmap.ic_pingzhi_star_yello);
            starBarBean.dividerHeight = 5;
            starBarBeens.add(starBarBean);
        }
        vStarBar.setStarBarBeanList(starBarBeens);
    }

}
