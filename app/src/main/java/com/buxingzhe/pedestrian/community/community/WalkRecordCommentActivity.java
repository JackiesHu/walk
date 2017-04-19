package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;

/**
 * Created by QJ on 2017/4/19.
 */

public class WalkRecordCommentActivity extends BaseActivity {
    private String walkRecord;//记录id
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commact_info);
        mContext = this;
        walkRecord = (String) getIntent().getSerializableExtra(CommActFragment.WALKACTIVITYINFO);
        initView();
//        setData();
        setTitleBar();
    }

    private void initView() {
    }

    private void setTitleBar() {
    }
}
