package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.activity.ImagesGridActivity;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.List;

/**·
 * Created by jackie on 2017/2/12.
 */

public class WalkDetialsDiscussActivity extends BaseActivity implements View.OnClickListener,
        AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{
    private LinearLayout vAddress;
    private AndroidImagePicker mImagePicker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakl_discuss);
        findId();
        setOnclick();
        mImagePicker = AndroidImagePicker.getInstance();
    }

    private void setOnclick() {
        vAddress.setOnClickListener(this);
    }

    private void findId() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        vAddress = (LinearLayout) findViewById(R.id.addAddress);
        setTitle("评论");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addAddress:
                selectImage();
            break;
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
