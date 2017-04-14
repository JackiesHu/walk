package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.widget.TitleBarView;

/**
 * Created by jackie on 2017/3/12.
 */

public class CommActInfoActivity extends BaseActivity {
    private ListView refreshStaggeredGridView;
    private View headView;
    private ImageView head_InfoImageView;
    private TextView head_InfoTitle;
    private TextView head_Infoinfo;
    private ToggleButton head_ToggleButton;
    private LinearLayout info_linear;
    private TextView head_sm_TextView;
    private TextView head_yq_TextView;
    private ImageView back_imageView;
    private ImageView share_imageView;
    private RelativeLayout rl_user;
    private LinearLayout head_InfoMsg;
    private ImageView show_hide_imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commact_info);
        fiId();
        setTitle();
        initHeadView();

    }
    private void fiId() {
        vTitleBar = (TitleBarView) findViewById(R.id.actinfo_title_bar);
        refreshStaggeredGridView = (ListView) findViewById(R.id.activity_Info_GridView);
        CommActInfoAdapter vAapter = new CommActInfoAdapter(this,this);
        refreshStaggeredGridView.setAdapter(vAapter);
    }
    private void setTitle() {
        setTitle("南京步行街-黄浦江夜行");
        setRightIco(R.mipmap.ic_shequ_share);
    }
    private void initHeadView(){
        headView= View.inflate(mContext,R.layout.activity_info_head,null);
        info_linear= (LinearLayout) headView.findViewById(R.id.info_linear);
        head_InfoImageView= (ImageView) headView.findViewById(R.id.head_InfoImageView);
        head_InfoTitle= (TextView) headView.findViewById(R.id.head_InfoTitle);
        head_Infoinfo= (TextView) headView.findViewById(R.id.head_Infoinfo);
        head_ToggleButton= (ToggleButton) headView.findViewById(R.id.head_ToggleButton);
        head_InfoMsg= (LinearLayout) headView.findViewById(R.id.head_InfoMsg);
        head_InfoMsg.setVisibility(View.GONE);
        back_imageView= (ImageView)headView. findViewById(R.id.back_imageView);
        share_imageView= (ImageView)headView. findViewById(R.id.share_imageView);
        show_hide_imageView= (ImageView) headView.findViewById(R.id.show_hide_imageView);
        refreshStaggeredGridView.addHeaderView(headView);
    }
}
