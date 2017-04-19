package com.buxingzhe.pedestrian.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.buxingzhe.pedestrian.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Administrator on 2016/5/20.
 */
public class PicassManager {
    private static PicassManager s_instance;

    private PicassManager() {

    }

    /**
     * 缓存目录 /data/data/<application package>/cache/picasso-cache/下边
     */
//   private void loadImageCache() {
//        //可以只定目录
//        final String imageCacheDir = "/data/data/com.quanjing.yoda/cache/image/";
//
////       File cacheDir = StorageUtils.getOwnCacheDirectory(mContext, "com.quanjing/weitu/imageloader/Cache");
//        Picasso picasso = new Picasso.Builder(context).downloader(
//                new OkHttpDownloader(new File(imageCacheDir))).build();
//        Picasso.setSingletonInstance(picasso);
//    }
    public static PicassManager getInstance() {
        if (s_instance == null) {
            s_instance = new PicassManager();
        }
        return s_instance;
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
    public void load(Context context, String imageUrl, int width, int height, ImageView imageView, Callback callback) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).resize(width, height).error(R.drawable.default_img).into(imageView, callback);
    }


    /**
     * 加载头像
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadAvatar(Context context, String imageUrl, int width, int height, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).placeholder(R.mipmap.ic_me_user_non_big).resize(width, height).error(R.mipmap.ic_me_user_non_big).centerCrop().into(imageView);
    }

    /**
     * 加载头像
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadAvatar(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).placeholder(R.mipmap.ic_me_user_non_big).error(R.mipmap.ic_me_user_non_big).into(imageView);
    }

    /**
     * 加载头像
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadAvatar(Context context, String imageUrl, int width, int height, ImageView imageView, Callback callback) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).placeholder(R.mipmap.ic_me_user_non_big).resize(width, height).error(R.mipmap.ic_me_user_non_big).into(imageView, callback);
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

    public int[] getLocalImageDis(String imageUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);/* 这里返回的bmp是null */
        int widht = options.outWidth;
        int height = options.outHeight;

        int[] ints = {widht, height};
        return ints;
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
