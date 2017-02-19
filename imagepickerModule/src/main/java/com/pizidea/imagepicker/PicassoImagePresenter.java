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

package com.pizidea.imagepicker;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * <b>desc your class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class PicassoImagePresenter implements ImagePresenter{
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Picasso.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                //.dontAnimate()
                //.thumbnail(0.5f)
                //.override(size, size)
                .resize(size/4*3, size/4*3)
                .placeholder(R.drawable.default_img)
                //.error(R.drawable.default_img)
                .into(imageView);

    }

    @Override
    public void loadLocalImage(Context context,ImageView imageView, String imageUri) {
        if (TextUtils.isEmpty(imageUri)){
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUri, options);/* 这里返回的bmp是null */
        int widht = options.outWidth;
        int height = options.outHeight;

        if (widht <= 0 || height <= 0){
            return;
        }

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeigh = wm.getDefaultDisplay().getHeight();

        int sizeW = widht > screenWidth ? screenWidth : widht;
        int sizeH = height > screenHeigh ? screenHeigh : height;
        Picasso.with(context).load(new File(imageUri)).resize(sizeW,sizeH).placeholder(R.drawable.default_img). error(R.drawable.default_img).into(imageView);
    }
    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadLocal(Context context, String imageUrl, int width, int height, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(new File(imageUrl)).resize(width, height).error(R.drawable.default_img).into(imageView);
    }
    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadLocal(Context context, String imageUrl, int width,ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);/* 这里返回的bmp是null */

        int outheight = options.outHeight;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int sizeW = width > outheight ? outheight : width;
        int screenWidth = wm.getDefaultDisplay().getWidth();
        sizeW = sizeW > screenWidth ? screenWidth : sizeW;
        int sizeH = sizeW/screenWidth*outheight;
        Picasso.with(context).load(new File(imageUrl)).resize(sizeW, sizeH).error(R.drawable.default_img).into(imageView);
    }
    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadLocal(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);/* 这里返回的bmp是null */
        int widht = options.outWidth;
        int height = options.outHeight;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int screenWidth = wm.getDefaultDisplay().getWidth();

        int sizeW = widht > screenWidth ? screenWidth : widht;
        int sizeH = sizeW/widht*height;
        Picasso.with(context).load(new File(imageUrl)).error(R.drawable.default_img).resize(sizeW, sizeH).into(imageView);
    }
    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void load(Context context, String imageUrl, int width, int height, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).resize(width, height).error(R.drawable.default_img).into(imageView);
    }
    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void load(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).error(R.drawable.default_img).into(imageView);
    }
}
