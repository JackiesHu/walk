package com.buxingzhe.pedestrian.community.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.activity.ImagesGridActivity;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.List;

/**
 * Created by mzyr on 2017/4/18.
 */

public class PublishActActivity extends BaseActivity implements View.OnClickListener,
        AndroidImagePicker.OnPictureTakeCompleteListener, AndroidImagePicker.OnImagePickCompleteListener {
    private AndroidImagePicker mImagePicker;

    private ImageView iv_upload_pic;
    private ImageView iv_select_pic;
    private ImageView iv_deletepic;
    private EditText et_title;
    private RelativeLayout startTimeRL;
    private RelativeLayout endTimeRL;
    private TextView tv_start;
    private TextView tv_end;
    private EditText et_introduction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_act);
        initView();
        initOnClick();
        mImagePicker = AndroidImagePicker.getInstance();
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle(getResources().getString(R.string.publish_activity));
        setRightTitle(getResources().getString(R.string.finish));

        iv_upload_pic = (ImageView) findViewById(R.id.iv_upload_pic);
        iv_select_pic = (ImageView) findViewById(R.id.iv_select_pic);
        iv_deletepic = (ImageView) findViewById(R.id.iv_select_pic);
        et_title = (EditText) findViewById(R.id.et_title);
        et_introduction = (EditText) findViewById(R.id.et_introduction);
        startTimeRL = (RelativeLayout) findViewById(R.id.startTimeRL);
        endTimeRL = (RelativeLayout) findViewById(R.id.endTimeRL);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);
    }

    private void initOnClick() {
        iv_upload_pic.setOnClickListener(this);
        iv_deletepic.setOnClickListener(this);
        startTimeRL.setOnClickListener(this);
        endTimeRL.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_upload_pic:
                selectImage();
                break;
            case R.id.iv_deletepic:
                isHavePic(false);
                break;
            case R.id.startTimeRL:
                break;
            case R.id.endTimeRL:
                break;
        }
    }

    /**
     * 是否有选中的照片
     * @param isHave
     */
    private void isHavePic(boolean isHave) {
        if (isHave){
            iv_upload_pic.setVisibility(View.GONE);
            iv_select_pic.setVisibility(View.VISIBLE);
            iv_deletepic.setVisibility(View.VISIBLE);
        }else{
            iv_upload_pic.setVisibility(View.VISIBLE);
            iv_select_pic.setVisibility(View.GONE);
            iv_deletepic.setVisibility(View.GONE);
        }
    }


//    public void selectDate() {
//        DatePicker picker = new DatePicker(this);
//        picker.setAnimationStyle(R.style.Animation_CustomPopup);
//        int todayYear = MethUserLoginManager.getTodayYear();
//        picker.setRange(1900, todayYear);//年份范围
//        picker.setTextSize(16);
//        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
//            @Override
//            public void onDatePicked(String year, String month, String day) {
//                String date = MethUserLoginManager.formatBirth(year, month, day);
//                vUserBirthDay.setText(date);
//                mYear = Integer.parseInt(year);
//                mMonth = Integer.parseInt(month);
//                mDay = Integer.parseInt(day);
//            }
//        });
//        if (mYear > 0) {
//            picker.setSelectedItem(mYear, mMonth, mDay);
//        } else {
//            int selectDate = todayYear - 20;
//            picker.setSelectedItem(selectDate, 6, 15);
//        }
//
//        picker.setCancelText("   取消");
//        picker.setSubmitText("确定   ");
//        picker.setTextColor(SystemUtils.getByColor(R.color.table_title_black));
//        picker.show();
//    }

    private void selectImage() {
        mImagePicker.setOnImagePickCompleteListener(this);
        mImagePicker.setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
        mImagePicker.setShouldShowCamera(true);
        Intent intent = new Intent();
        intent.putExtra("isCrop", false);
        intent.setClass(this, ImagesGridActivity.class);
        EnterActUtils.startAct(mActivity, intent);

    }


    @Override
    public void onImagePickComplete(List<ImageItem> items) {

    }

    @Override
    public void onPictureTakeComplete(String picturePath) {

    }
}
