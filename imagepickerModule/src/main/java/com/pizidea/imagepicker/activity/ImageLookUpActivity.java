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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.bean.ImageUpSerialize;
import com.pizidea.imagepicker.ui.ImagePreviewFragment;

import java.util.List;

public class ImageLookUpActivity extends FragmentActivity {
    private static final String TAG = ImagePreviewFragment.class.getSimpleName();
    public static final String DATA = "data";
    public static final String POSITION = "position";
    Activity mContext;

    ViewPager mViewPager;
    TouchImageAdapter mAdapter ;
    ImageUpSerialize imageUpSerialize;
    List<ImageItem> mImageList;
    private int mCurrentItemPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(com.pizidea.imagepicker.R.layout.fragment_preview);
        getArgment();
        initView();
    }
    private void getArgment() {
        imageUpSerialize = (ImageUpSerialize)getIntent().getSerializableExtra("data");
        mCurrentItemPosition = getIntent().getIntExtra("position",-1);
        mImageList = imageUpSerialize.items;
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(com.pizidea.imagepicker.R.id.viewpager);
        mAdapter = new TouchImageAdapter(((FragmentActivity)mContext).getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentItemPosition, false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
            }
            @Override public void onPageScrollStateChanged(int state) { }

        });

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
            SinglePreviewLookFragment fragment = new SinglePreviewLookFragment(mContext,mImageList.get(position).path);
            Bundle bundle = new Bundle();
            bundle.putSerializable(SinglePreviewFragment.KEY_URL, mImageList.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

    }


}
