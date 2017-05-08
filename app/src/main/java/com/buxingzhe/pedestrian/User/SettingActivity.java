package com.buxingzhe.pedestrian.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.activity.SplashActivity;
import com.buxingzhe.pedestrian.utils.DataCleanManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.widget.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity {

    @BindView(R.id.clearCacheTv)
    TextView clearCacheTv;
    @BindView(R.id.changeUserInfoTv)
    TextView changeUserInfoTv;
    @BindView(R.id.exitTv)
    TextView exitTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle("设置");
    }

    @OnClick({R.id.clearCacheTv, R.id.changeUserInfoTv, R.id.exitTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearCacheTv:
                clearAppCache();
                break;
            case R.id.changeUserInfoTv:
                Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
                EnterActUtils.startForResultAct(SettingActivity.this, intent, 1);
                break;
            case R.id.exitTv:
                Intent intent1 = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void clearAppCache() {
        DataCleanManager.cleanInternalCache(SettingActivity.this);
        Toast.makeText(SettingActivity.this, "缓存已清除哦", Toast.LENGTH_SHORT).show();
    }
}
