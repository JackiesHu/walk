package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.widget.TitleBarView;

/**
 * Created by jackie on 2017/3/12.
 */

public class CommActInfoActivity extends BaseActivity {
    private WalkActivityInfo walkActivityInfo;
    private ListView refreshStaggeredGridView;
    private View headView;
    private ImageView iv_banner;
    private TextView tv_attend;
    private TextView tv_introduction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commact_info);
        walkActivityInfo = (WalkActivityInfo) getIntent().getSerializableExtra(CommActFragment.WALKACTIVITYINFO);
        initView();
        setTitleBar();
        initHeadView();

    }
    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.actinfo_title_bar);
        refreshStaggeredGridView = (ListView) findViewById(R.id.activity_Info_GridView);
        CommActInfoAdapter vAapter = new CommActInfoAdapter(this,this);
        refreshStaggeredGridView.setAdapter(vAapter);
    }
    private void setTitleBar() {
        if (!TextUtils.isEmpty(walkActivityInfo.getTitle())){
            setTitle(walkActivityInfo.getTitle());
        }
        setRightIco(R.mipmap.ic_shequ_share);
    }
    private void initHeadView(){
        headView= View.inflate(mContext,R.layout.activity_info_head,null);
        iv_banner= (ImageView) headView.findViewById(R.id.iv_banner);
        tv_attend= (TextView) headView.findViewById(R.id.tv_attend);
        tv_introduction= (TextView) headView.findViewById(R.id.tv_introduction);

        refreshStaggeredGridView.addHeaderView(headView);
    }
}
