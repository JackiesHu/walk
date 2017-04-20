package com.buxingzhe.pedestrian.community.community;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.NetRequestParams;
import com.buxingzhe.pedestrian.http.imageupload.UploadImage;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.activity.ImagesGridActivity;
import com.pizidea.imagepicker.bean.ImageItem;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by mzyr on 2017/4/18.
 */

public class PublishActActivity extends BaseActivity implements View.OnClickListener,
        AndroidImagePicker.OnPictureTakeCompleteListener, AndroidImagePicker.OnImagePickCompleteListener {
    private AndroidImagePicker mImagePicker;
    private String  localUrl;

    private ImageView iv_upload_pic;
    private ImageView iv_select_pic;
    private ImageView iv_deletepic;
    private EditText et_title;
    private RelativeLayout startTimeRL;
    private RelativeLayout endTimeRL;
    private TextView tv_start;
    private TextView tv_end;
    private EditText et_introduction;

    private AsyncTask asyncTask;

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
        iv_deletepic = (ImageView) findViewById(R.id.iv_deletepic);
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
    public void onRightListener(View v) {
        super.onRightListener(v);

        String title = et_title.getText().toString();
        String startTime = tv_start.getText().toString();
        String endTime = tv_end.getText().toString();
        String introduction = et_introduction.getText().toString();
        if (!checkEmpty(title, startTime, endTime, introduction)) {
            return;
        }

        Long startTimestamp = null;
        Long endTimestamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            startTimestamp = startDate.getTime();
            endTimestamp = endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", GlobalParams.USER_ID);
        paramsMap.put("token", GlobalParams.TOKEN);
        paramsMap.put("title", title);
        paramsMap.put("startTimestamp", startTimestamp.toString());
        paramsMap.put("endTimestamp", endTimestamp.toString());
        paramsMap.put("introduction", introduction);
        paramsMap.put("publisher", GlobalParams.USER_ID);
        initTask(paramsMap);
    }

    private boolean checkEmpty(String title, String startTime, String endTime, String introduction) {
        if (TextUtils.isEmpty(localUrl)) {
            Toast.makeText(mContext, getString(R.string.activity_select_mainpic), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(mContext, getString(R.string.activity_input_title), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(startTime)) {
            Toast.makeText(mContext, getString(R.string.activity_select_start), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(mContext, getString(R.string.activity_select_end), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                selectStartTime();
                break;
            case R.id.endTimeRL:
                selectEndTime();
                break;
        }
    }

    /**
     * 是否有选中的照片
     *
     * @param isHave
     */
    private void isHavePic(boolean isHave) {
        if (isHave) {
            PicassManager.getInstance().loadLocal(mContext,localUrl,iv_select_pic);
            iv_upload_pic.setVisibility(View.GONE);
            iv_select_pic.setVisibility(View.VISIBLE);
            iv_deletepic.setVisibility(View.VISIBLE);
        } else {
            localUrl = "";
            iv_upload_pic.setVisibility(View.VISIBLE);
            iv_select_pic.setVisibility(View.GONE);
            iv_deletepic.setVisibility(View.GONE);
        }
    }

    int mYear;
    int mMonth;
    int mDay;

    /**
     * 时间选择器
     */
    public void selectStartTime() {
        DatePicker picker = new DatePicker(this);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        int[] dates = getDates();
        int todayYear = dates[0];
        picker.setRange(todayYear, 2100);//年份范围
        picker.setTextSize(16);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String date = formatBirth(year, month, day);
                mYear = Integer.parseInt(year);
                mMonth = Integer.parseInt(month);
                mDay = Integer.parseInt(day);
                tv_start.setText(date);
            }
        });
        if (mYear > 0) {
            picker.setSelectedItem(mYear, mMonth, mDay);
        } else {
            mYear = dates[0];
            mMonth = dates[1];
            mDay = dates[2];
            picker.setSelectedItem(mYear, mMonth, mDay);
        }

        picker.setCancelText("   取消");
        picker.setSubmitText("确定   ");
        picker.setTextColor(Color.BLACK);
        picker.show();
    }


    public void selectEndTime() {
        DatePicker picker = new DatePicker(this);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        int[] dates = getDates();
        int todayYear = dates[0];
        picker.setRange(todayYear, 2100);//年份范围
        picker.setTextSize(16);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String date = formatBirth(year, month, day);
                tv_end.setText(date);
            }
        });
        if (mYear > 0) {
            picker.setRangeStart(mYear, mMonth, mDay);
        } else {
            picker.setSelectedItem(getDates()[0], getDates()[1], getDates()[2]);
        }
        picker.setCancelText("   取消");
        picker.setSubmitText("确定   ");
        picker.setTextColor(Color.BLACK);
        picker.show();
    }

    /**
     * 获取当前年月日
     *
     * @return
     */
    public int[] getDates() {
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int date = mCalendar.get(Calendar.DATE);

        int[] dates = new int[]{year, month, date};
        return dates;
    }

    public String formatBirth(String year, String month, String day) {
        String dates = year + "-" + month + "-" + day;
        return dates;
    }

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
        if (items != null && items.size() > 0){
            ImageItem imageItem = items.get(0);
            localUrl = imageItem.path;
            isHavePic(true);
        }else{
            isHavePic(false);
        }
    }

    @Override
    public void onPictureTakeComplete(String picturePath) {
        if (!TextUtils.isEmpty(picturePath)){
            localUrl = picturePath;
            isHavePic(true);
        }else{
            isHavePic(false);
        }
    }

    private void initTask(final Map<String,String> paramsMap){
        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String response = UploadImage.uploadFile(new File(localUrl),paramsMap,NetRequestParams.PUBLISHACTIVITY);
                return response;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(PublishActActivity.this, "开始上传", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(PublishActActivity.this, (o == null ? "" : o.toString()), Toast.LENGTH_SHORT).show();
            }
        };
    }
}

