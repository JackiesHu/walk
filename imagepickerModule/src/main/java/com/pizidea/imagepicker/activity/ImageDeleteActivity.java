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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.R;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.bean.ImageUpSerialize;

import java.util.List;

public class ImageDeleteActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = ImageDeleteActivity.class.getSimpleName();
    private boolean enableSingleTap = true;//singleTap to do something
    TextView mTitleCount;
    TextView mBtnOk;

    List<ImageItem> mImageList;
    int mShowItemPosition = 0;
    ImageUpSerialize imageUpSerialize;
    ViewPager mViewPager;
    TouchImageAdapter mAdapter ;
    Context mContext;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_delete);
        mContext = this;
        mActivity = this;
        imageUpSerialize = (ImageUpSerialize)getIntent().getSerializableExtra(AndroidImagePicker.KEY_PIC_PATH);
        mImageList = imageUpSerialize.items;

        mShowItemPosition = getIntent().getIntExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, 0);

        mBtnOk = (TextView) findViewById(R.id.btn_ok);
        mViewPager = (ViewPager)findViewById(R.id.delete_viewpager);
        mBtnOk.setOnClickListener(this);


        mTitleCount = (TextView) findViewById(R.id.tv_title_count);
        mTitleCount.setText(mShowItemPosition + "/" + mImageList.size());

        //back press
        findViewById(R.id.btn_backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        mAdapter = new TouchImageAdapter(((FragmentActivity)mContext).getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        int index = mShowItemPosition - 1 < 0 ? 0:mShowItemPosition - 1;
        mViewPager.setCurrentItem(index, false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mShowItemPosition = position +1;
                mTitleCount.setText(mShowItemPosition + "/" + mImageList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_pic_rechoose) {
            finish();

        } else if (i == R.id.btn_ok) {
            deleteSelectImage();

        } else {
        }
    }
    public void onImageSingleTap(MotionEvent e) {
        View topBar = findViewById(R.id.top_bar);
        View bottomBar = findViewById(R.id.bottom_bar);
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(ImageDeleteActivity.this, R.anim.top_out));
            //bottomBar.setAnimation(AnimationUtils.loadAnimation(ImageDeleteActivity.this, R.anim.fade_out));
            topBar.setVisibility(View.GONE);
            //bottomBar.setVisibility(View.GONE);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(ImageDeleteActivity.this, R.anim.top_in));
            //bottomBar.setAnimation(AnimationUtils.loadAnimation(ImageDeleteActivity.this, R.anim.fade_in));
            topBar.setVisibility(View.VISIBLE);
            //bottomBar.setVisibility(View.VISIBLE);
        }

    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "=====removeOnImageItemSelectedListener");
        super.onDestroy();
    }
    public void deleteSelectImage(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.please_confirm))
                .setMessage(getResources().getString(R.string.confirmed_that_the_cancellation_of_the_picture))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mImageList.size() == 1) {
                            mImageList.clear();
                            AndroidImagePicker.getInstance().notifyOnImagePageSelectedListener(mImageList);
                            setResult(RESULT_OK);
                           finish();
                        } else {
                            mShowItemPosition--;
                            if (mShowItemPosition < 0) {
                                mShowItemPosition = 0;
                            }
                            refreshAapter();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }
    public void refreshAapter(){
        mImageList.remove(mShowItemPosition);
        mViewPager.removeAllViews();
        mAdapter = new TouchImageAdapter(((FragmentActivity)mContext).getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mShowItemPosition, false);
        AndroidImagePicker.getInstance().notifyOnImagePageSelectedListener(mImageList);
        if (mShowItemPosition == 0){
            mShowItemPosition ++;
        }
        mTitleCount.setText(mShowItemPosition +"/" + (mImageList.size()));
    }
    class TouchImageAdapter extends FragmentStatePagerAdapter {
        public TouchImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Fragment getItem(int position) {
           SinglePreviewFragment fragment = new SinglePreviewFragment(mContext,mImageList.get(position).path);
            return fragment;
        }

    }


}
