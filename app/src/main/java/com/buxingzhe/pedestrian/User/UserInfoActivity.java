package com.buxingzhe.pedestrian.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.utils.FileConfig;
import com.buxingzhe.pedestrian.utils.PicUtil;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

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
    protected void onResume() {
        super.onResume();
        initUser();
    }
    private void initUser() {
        UserInfo userInfo = new UserInfo();
        userInfo = userInfo.getUserInfo(this);
        // 头像
        if(!TextUtils.isEmpty(userInfo.getAvatar())){
            Picasso.with(this).load(userInfo.getAvatar()).resize(SystemUtils.dip2px(this,67.0f),SystemUtils.dip2px(this,67.0f)).centerCrop()
                    .into((ImageView) findViewById(R.id.sdv_userHead));
        }
        // 昵称
        ((TextView) findViewById(R.id.tv_userName)).setText(userInfo.getNickName());
        // 性别
        if (userInfo.getGender().equals("m"))
            ((ImageView) findViewById(R.id.iv_userSex)).setImageResource(R.mipmap.personal_icon_m);
        else
            ((ImageView) findViewById(R.id.iv_userSex)).setImageResource(R.mipmap.personal_icon_f);
        // 年龄
        ((TextView) findViewById(R.id.tv_userAge)).setText(userInfo.getCreateTimestamp() + "岁");
        //
        if (userInfo.getHeight().equals(""))
            findViewById(R.id.tv_userHeight).setVisibility(View.INVISIBLE);
        else {
            findViewById(R.id.tv_userHeight).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_userHeight)).setText(userInfo.getHeight() + "cm");
        }
        ((TextView) findViewById(R.id.tv_userWeight)).setText(userInfo.getWeight() + "kg");
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
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == PicUtil.CUT_PIC_CODE) {
            String newFileName = FileConfig.IMAGE_UP_PATH + System.currentTimeMillis() + ".jpg";
            try {
                FileUtil.compressBmpToFile(PicUtil.cutPicPath, newFileName);

//                File f = new File(PicUtil.takePicturePath);
//                if (f.exists())
//                    f.delete();
                File f = new File(PicUtil.cutPicPath);
                if (f.exists())
                    f.delete();
                PicUtil.cutPicPath = newFileName;
                if(!TextUtils.isEmpty(newFileName)){
                    Picasso.with(this).load(new File(newFileName)).resize(SystemUtils.dip2px(this,67.0f),SystemUtils.dip2px(this,67.0f)).centerCrop()
                            .into((ImageView) findViewById(R.id.sdv_userHead));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (arg0 == PicUtil.TAKEPHOTO_PIC_CODE || arg0 == PicUtil.SELECT_PIC_CODE) {
            if (arg1 == RESULT_OK) {
                PicUtil.cutPhotoZoom(arg2, this);
            }
        }
    }
}
