/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.R;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.ui.ImagesGridFragment;

import java.util.ArrayList;
import java.util.List;

public class ImagesGridActivity extends FragmentActivity implements View.OnClickListener,AndroidImagePicker.OnImageSelectedListener,
        AndroidImagePicker.OnPictureTakeCompleteListener,AdapterView.OnItemClickListener,AndroidImagePicker.OnImageCropCompleteListener {
    private static final String TAG = ImagesGridActivity.class.getSimpleName();

    private TextView mBtnOk;

    ImagesGridFragment mFragment;
    AndroidImagePicker androidImagePicker;
    String imagePath;
    private boolean mIsCrop;
    private boolean mIsClearSelectedImage = true;
    private int selectLimit = 9;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setTranslucentStatus();
        setContentView(R.layout.activity_images_grid);
        init();
    }
    protected void setTranslucentStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void init(){
        getExtra();
        androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.setSelectLimit(selectLimit);
        //most of the time you need to clear the last selected images or you can comment out this line
        if (mIsClearSelectedImage){
            androidImagePicker.clearSelectedImages();
        }
        initById();
        /*添加fragmetn*/
        mFragment = new ImagesGridFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();

        addLinstener();

        /*设置完成的个数*/
        int selectedCount = androidImagePicker.getSelectImageCount();
        onImageSelected(0, null, selectedCount, androidImagePicker.getSelectLimit());


    }

    private void addLinstener() {
        mBtnOk.setOnClickListener(this);
        androidImagePicker.setOnPictureTakeCompleteListener(this);//watching Picture taking
        mFragment.setOnImageItemClickListener(this);
        androidImagePicker.addOnImageSelectedListener(this);
        androidImagePicker.addOnImageCropCompleteListener(this);
    }

    private void initById() {
        mBtnOk = (TextView) findViewById(R.id.btn_ok);

        if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE){
            mBtnOk.setVisibility(View.GONE);
        }else{
            mBtnOk.setVisibility(View.VISIBLE);
        }
    }

    private void getExtra(){
        mIsCrop = getIntent().getBooleanExtra("isCrop", true);
        mIsClearSelectedImage = getIntent().getBooleanExtra("isClearSelectedImage",true);
        selectLimit = getIntent().getIntExtra("selectLimit", 9);
        imagePath = getIntent().getStringExtra(AndroidImagePicker.KEY_PIC_PATH);
    }
    public void BackOnclick(View view){
       finish();
    }
    /**
     * 预览页面
     * @param position
     */
    private void go2Preview(int position) {
        Intent intent = new Intent();
        intent.putExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, position);
        intent.setClass(ImagesGridActivity.this, ImagePreviewActivity.class);
        startActivityForResult(intent, AndroidImagePicker.REQ_PREVIEW);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_pic_rechoose) {
            finish();

        } else if (i == R.id.btn_ok) {
            androidImagePicker.notifyOnImagePickComplete(androidImagePicker.getSelectedImages());
            setResult(RESULT_OK);
            finish();

        } else {
        }

    }
    @Override
    public void onImageSelected(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        if(selectedItemsCount > 0){
            mBtnOk.setEnabled(true);
            //mBtnOk.setText("完成("+selectedItemsCount+"/"+maxSelectLimit+")");
            mBtnOk.setText(getResources().getString(R.string.select_complete,selectedItemsCount+"",maxSelectLimit+""));
        }else{
            mBtnOk.setText(getResources().getString(R.string.complete));
            mBtnOk.setEnabled(false);
        }
        Log.i(TAG, "=====EVENT:onImageSelected");
    }

    @Override
    protected void onDestroy() {
        androidImagePicker.removeOnImageItemSelectedListener(this);
        androidImagePicker.removeOnPictureTakeCompleteListener(this);
        androidImagePicker.removeOnImageCropCompleteListener(this);
        Log.i(TAG, "=====removeOnImageItemSelectedListener");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == AndroidImagePicker.REQ_CAMERA){
                Bitmap bmp = (Bitmap)data.getExtras().get("bitmap");
                Log.i(TAG, "=====get Bitmap:" + bmp.hashCode());
            }else if(requestCode == AndroidImagePicker.REQ_PREVIEW){
                androidImagePicker.notifyOnImagePickComplete(androidImagePicker.getSelectedImages());
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onPictureTakeComplete(String picturePath) {
        /*if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE){*/
        if(mIsCrop){
            Intent intent = new Intent();
            intent.setClass(ImagesGridActivity.this,ImageCropActivity.class);
            intent.putExtra(AndroidImagePicker.KEY_PIC_PATH,picturePath);
            startActivityForResult(intent, AndroidImagePicker.REQ_CAMERA);
        }else {
            setResult(RESULT_OK);
            finish();
        }
        /*}*/
    }
    @Override
    public void onImageCropComplete(Bitmap bmp, float ratio) {
        setResult(RESULT_OK);
        finish();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = androidImagePicker.isShouldShowCamera() ? position - 1 : position;
        if (androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_MULTI) {
            go2Preview(position);
        } else if (androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE) {
            if (mIsCrop) {
                Intent intent = new Intent();
                intent.setClass(ImagesGridActivity.this, ImageCropActivity.class);
                intent.putExtra(AndroidImagePicker.KEY_PIC_PATH, androidImagePicker.getImageItemsOfCurrentImageSet().get(position).path);
                startActivityForResult(intent, AndroidImagePicker.REQ_CAMERA);
            } else {
                androidImagePicker.clearSelectedImages();
                androidImagePicker.addSelectedImageItem(position, androidImagePicker.getImageItemsOfCurrentImageSet().get(position));
                List<ImageItem> imageItemList = new ArrayList<ImageItem>();
                ImageItem imageItem = androidImagePicker.getImageItemsOfCurrentImageSet().get(position);
                imageItemList.add(imageItem);
                AndroidImagePicker.getInstance().notifyOnImagePickComplete(imageItemList);
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
