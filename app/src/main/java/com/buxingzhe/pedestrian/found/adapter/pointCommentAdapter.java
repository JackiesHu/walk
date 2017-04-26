package com.buxingzhe.pedestrian.found.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.bean.Comment;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.widget.MWTStarBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by hasee on 2017/4/24.
 */

public class pointCommentAdapter extends RecycleBaseAdapter<Comment> {

    public pointCommentAdapter(Context context, List<Comment> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public void convert(RecycleViewHolder holder, Comment comment, int position) {

        TextView iv_user_name = holder.getView(R.id.iv_user_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        MWTStarBar vWalkUserDisStar = holder.getView(R.id.walkUserDis_starBar);

        iv_user_name.setText(comment.getUser().getNickName());
        tv_content.setText(comment.getContent());
        PicassManager.getInstance().load(context, comment.getUser().getAvatarUrl(), (ImageView) holder.getView(R.id.iv_avatar));
        List<StarBarBean> userStarBars = new ArrayList<>();
        StarBarBean starBarBean;
        for (int i =0;i<comment.getSafeStar();i++){
            starBarBean = new StarBarBean(R.mipmap.ic_pingzhi_star_yello);
            userStarBars.add(starBarBean);
        }
        vWalkUserDisStar.setStarBarBeanList(userStarBars);
    }
}
