package com.buxingzhe.pedestrian.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.utils.PicUtil;
import com.buxingzhe.pedestrian.widget.TitleBarView;

/**
 * // 用户信息详情
 * Created by Administrator on 2016/4/7 0007.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    String headUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();
    }
    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle("个人资料");
        findViewById(R.id.ll_userHead).setOnClickListener(this);
        findViewById(R.id.ll_userName).setOnClickListener(this);
        findViewById(R.id.ll_userSex).setOnClickListener(this);
        findViewById(R.id.ll_userAge).setOnClickListener(this);
        findViewById(R.id.ll_userHeight).setOnClickListener(this);
        findViewById(R.id.ll_userWeight).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(IntentKey.EDIT_TAG, view.getId());
        switch (view.getId()) {
            case R.id.ll_userHead:

                PicUtil.showPicDialog(this);
                break;
            case R.id.ll_userName:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userSex:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userAge:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userHeight:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userWeight:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
        }
        if (intent.getComponent() != null)
            startActivity(intent);
    }
}
