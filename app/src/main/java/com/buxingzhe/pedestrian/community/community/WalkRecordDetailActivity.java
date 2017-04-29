package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.walk.umengshare.UmengShareUtils;

import java.text.DecimalFormat;

/**
 * Created by jackie on 2017/3/12.
 */

public class WalkRecordDetailActivity extends BaseActivity implements View.OnClickListener {
    private WalkRecordInfo walkRecordInfo;
    private ImageView iv_route;
    private TextView tv_stepCount;
    private TextView tv_distance;
    private TextView tv_duration;
    private TextView tv_altitudeAsend;
    private TextView tv_altitudeHigh;
    private TextView tv_altitudeLow;
    private TextView tv_calorie;
    private TextView tv_fat;
    private TextView tv_nutrition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkrecord_detail);
        mContext = this;
        initView();
        setData();
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.actinfo_title_bar);

        iv_route = (ImageView) findViewById(R.id.iv_route);
        tv_stepCount = (TextView) findViewById(R.id.tv_stepCount);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_altitudeAsend = (TextView) findViewById(R.id.tv_altitudeAsend);
        tv_altitudeHigh = (TextView) findViewById(R.id.tv_altitudeHigh);
        tv_altitudeLow = (TextView) findViewById(R.id.tv_altitudeLow);
        tv_calorie = (TextView) findViewById(R.id.tv_calorie);
        tv_fat = (TextView) findViewById(R.id.tv_fat);
        tv_nutrition = (TextView) findViewById(R.id.tv_nutrition);
    }

    private void setTitleBar() {
        if (!TextUtils.isEmpty(walkRecordInfo.getCreateTime())) {
            Long t = Long.parseLong(walkRecordInfo.getCreateTime());
            String date = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new java.util.Date(t * 1000));
            setTitle(date);
        }
        setRightIco(R.mipmap.ic_shequ_share);
    }


    @Override
    public void onRightImageListener(View v) {
        super.onRightImageListener(v);
        share();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        }
    }

    private void setData() {
        walkRecordInfo = (WalkRecordInfo) getIntent().getParcelableExtra(CommCircleFragment.WALKRECORDINFO);
        if (walkRecordInfo == null) {
            return;
        }
        setTitleBar();
        if (!TextUtils.isEmpty(walkRecordInfo.getRoutepicStr())) {
            PicassManager.getInstance().load(mContext, walkRecordInfo.getRoutepicStr(), iv_route);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        tv_stepCount.setText(walkRecordInfo.getStepCount() + "");
        tv_distance.setText(walkRecordInfo.getDistance() + "");
        if (!TextUtils.isEmpty(walkRecordInfo.getDuration())) {
            tv_duration.setText(walkRecordInfo.getDuration());
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getAltitudeAsend())) {
            tv_altitudeAsend.setText(df.format(Double.parseDouble(walkRecordInfo.getAltitudeAsend())));
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getAltitudeHigh())) {
            tv_altitudeHigh.setText(df.format(Double.parseDouble(walkRecordInfo.getAltitudeHigh())));
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getAltitudeLow())) {
            tv_altitudeLow.setText(df.format(Double.parseDouble(walkRecordInfo.getAltitudeLow())));
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getCalorie())) {
            tv_calorie.setText(df.format(Double.parseDouble(walkRecordInfo.getCalorie())));
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getFat())) {
            tv_fat.setText(df.format(Double.parseDouble(walkRecordInfo.getFat())));
        }
        if (!TextUtils.isEmpty(walkRecordInfo.getNutrition())) {
            tv_nutrition.setText(df.format(Double.parseDouble(walkRecordInfo.getNutrition())));
        }
    }


    public void share() {
        UmengShareUtils.getInstall(mActivity).shareImageInfo("来自fdsfdsfde分享", "content", "https://ss1.bdstatic.com/5eN1bjq8AAUYm2zgoY3K/r/www/cache/holiday/habo/res/doodle/11.png", new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

            }
        }, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }
}
