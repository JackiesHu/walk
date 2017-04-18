package com.buxingzhe.pedestrian.community.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
        AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{
    private AndroidImagePicker mImagePicker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_act);
        findId();
        setOnClick();
        mImagePicker = AndroidImagePicker.getInstance();
    }

    private void setOnClick() {
//        vAddress.setOnClickListener(this);
    }

    private void findId() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle(getResources().getString(R.string.publish_activity));
        setRightTitle(getResources().getString(R.string.finish));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){


        }
    }
    private void selectImage(){
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
