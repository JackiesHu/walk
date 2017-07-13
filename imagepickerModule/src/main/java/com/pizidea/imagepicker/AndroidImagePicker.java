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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.bean.ImageSet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * <b>The main Entrance of this lib</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class AndroidImagePicker {
    public static final String TAG = AndroidImagePicker.class.getSimpleName();

    public static final int REQ_CAMERA = 1431;
    public static final int REQ_PREVIEW = 2347;


    public static final String KEY_PIC_PATH = "key_pic_path";
    public static final String KEY_PIC_SELECTED_POSITION = "key_pic_selected";

    private static AndroidImagePicker mInstance;

    public static AndroidImagePicker getInstance() {
        if (mInstance == null) {
            synchronized (AndroidImagePicker.class) {
                if (mInstance == null) {
                    mInstance = new AndroidImagePicker();
                }
            }
        }
        return mInstance;
    }

    private int selectLimit = 9;//can select 9 at most,you can change it yourself

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    private int selectMode = Select_Mode.MODE_MULTI;//Select mode:single or multi

    public int getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }

    private boolean shouldShowCamera = true;//indicate whether to show the camera item

    public boolean isShouldShowCamera() {
        return shouldShowCamera;
    }

    public void setShouldShowCamera(boolean shouldShowCamera) {
        this.shouldShowCamera = shouldShowCamera;
    }

    private String mCurrentPhotoPath;//image saving path when taking pictures

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    /**
     * Listeners of image selected changes,if you want to custom the Activity of ImagesGrid or ImagePreview,you might use it.
     */
    private List<OnImageSelectedListener> mImageSelectedListeners;

    public synchronized void addOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) {
            mImageSelectedListeners = new ArrayList<>();
            Log.i(TAG, "=====create new ImageSelectedListener List");
        }
        if (l != null) {
            this.mImageSelectedListeners.add(l);
            Log.i(TAG, "=====addOnImageSelectedListener:" + l.getClass().toString());
        }

    }

    public void removeOnImageItemSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) {
            return;
        }
        if (l != null && this.mImageSelectedListeners.size() > 0) {
            this.mImageSelectedListeners.remove(l);
            Log.i(TAG, "=====remove from mImageSelectedListeners:" + l.getClass().toString());
        }
    }

    private void notifyImageSelectedChanged(int position, ImageItem item, boolean isAdd) {
        if ((isAdd && getSelectImageCount() > selectLimit) || (!isAdd && getSelectImageCount() == selectLimit)) {
            //do not call the listeners if reached the select limit when selecting
            Log.i(TAG, "=====ignore notifyImageSelectedChanged:isAdd?" + isAdd);
        } else {
            if (mImageSelectedListeners == null || mImageSelectedListeners.size() <= 0) {
                return;
            }
            if (item != null) {
                Log.i(TAG, "=====notify mImageSelectedListeners:item=" + item.path);
                for (OnImageSelectedListener l : mImageSelectedListeners) {
                    l.onImageSelected(position, item, mSelectedImages.size(), selectLimit);
                }
            }

        }
    }

    /**
     * listeners of image crop complete
     */
    private List<OnImageCropCompleteListener> mImageCropCompleteListeners;

    public synchronized void addOnImageCropCompleteListener(OnImageCropCompleteListener l) {
        if (mImageCropCompleteListeners == null) {
            mImageCropCompleteListeners = new ArrayList<>();
            Log.i(TAG, "=====create new ImageCropCompleteListener List");
        }
        if (l != null) {
            this.mImageCropCompleteListeners.add(l);
            Log.i(TAG, "=====addOnImageCropCompleteListener:" + l.getClass().toString());
        }
    }

    public void removeOnImageCropCompleteListener(OnImageCropCompleteListener l) {
        if (mImageCropCompleteListeners == null) {
            return;
        }
        if (l != null && mImageCropCompleteListeners.size() > 0) {
            this.mImageCropCompleteListeners.remove(l);
            Log.i(TAG, "=====remove mImageCropCompleteListeners:" + l.getClass().toString());
        }

    }

    public synchronized void notifyImageCropComplete(Bitmap bmp, int ratio) {
        if (mImageCropCompleteListeners != null && mImageCropCompleteListeners.size() > 0 && bmp != null) {
            Log.i(TAG, "=====notify onImageCropCompleteListener  bitmap=" + bmp.toString() + "  ratio=" + ratio);
            for (OnImageCropCompleteListener l : mImageCropCompleteListeners) {
                l.onImageCropComplete(bmp, ratio);
            }
        }
    }

    /**
     * listener of picture taken
     */
    private ArrayList<OnPictureTakeCompleteListener> mOnPictureTakeCompleteListeners;

    public synchronized void setOnPictureTakeCompleteListener(OnPictureTakeCompleteListener l) {
        if (mOnPictureTakeCompleteListeners == null) {
            mOnPictureTakeCompleteListeners = new ArrayList<OnPictureTakeCompleteListener>();
        }
        if (l != null) {
            this.mOnPictureTakeCompleteListeners.add(l);
            Log.i(TAG, "=====setOnPictureTakeCompleteListener:" + l.getClass().toString());
        }

    }

    public synchronized void removeOnPictureTakeCompleteListener(OnPictureTakeCompleteListener l) {
        if (mOnPictureTakeCompleteListeners == null) {
            return;
        }
        if (l != null && mOnPictureTakeCompleteListeners.size() > 0) {
            this.mOnPictureTakeCompleteListeners.remove(l);
        }

    }

    public synchronized void notifyPictureTaken() {
        if (mOnPictureTakeCompleteListeners != null && mOnPictureTakeCompleteListeners.size() > 0) {
            for (OnPictureTakeCompleteListener l : mOnPictureTakeCompleteListeners) {
                l.onPictureTakeComplete(mCurrentPhotoPath);
            }

            Log.i(TAG, "=====notify mOnPictureTakeCompleteListener path=" + mCurrentPhotoPath);
        }
    }

    /**
     * Listener when image pick completed
     */
    private OnImagePickCompleteListener mOnImagePickCompleteListener;

    public synchronized void setOnImagePickCompleteListener(OnImagePickCompleteListener l) {
        if (l != null) {
            this.mOnImagePickCompleteListener = l;
            Log.i(TAG, "=====setOnImagePickCompleteListener:" + l.getClass().toString());
        }
    }

    public synchronized void deleteOnImagePickCompleteListener(OnImagePickCompleteListener l) {
        if (l != null) {
            mOnImagePickCompleteListener = null;
            Log.i(TAG, "=====remove mOnImagePickCompleteListener:" + l.getClass().toString());
            System.gc();
        }
    }

    public synchronized void notifyOnImagePickComplete(List<ImageItem> items) {
        if (mOnImagePickCompleteListener != null && items != null) {
            mOnImagePickCompleteListener.onImagePickComplete(items);
            Log.i(TAG, "=====notify mOnImagePickCompleteListener:selected size=" + items.size());
        }
    }

    private OnImagePageSelectedListener mOnImagePageSelectedListener;

    public void setOnImagePageSelectedListener(OnImagePageSelectedListener l) {
        if (l != null) {
            this.mOnImagePageSelectedListener = l;
            Log.i(TAG, "=====setOnImagePickCompleteListener:" + l.getClass().toString());
        }
    }

    public void deleteOnImagePageSelectedListener(OnImagePageSelectedListener l) {
        if (l != null) {
            mOnImagePageSelectedListener = null;
            Log.i(TAG, "=====remove mOnImagePickCompleteListener:" + l.getClass().toString());
            System.gc();
        }
    }

    public void notifyOnImagePageSelectedListener(List<ImageItem> items) {
        if (mOnImagePageSelectedListener != null && items != null) {
            mOnImagePageSelectedListener.onImagePageSelected(items);
            Log.i(TAG, "=====notify mOnImagePickCompleteListener:selected size=" + items.size());
        }
    }

    //All Images collect by Set
    private List<ImageSet> mImageSets;
    private int mCurrentSelectedImageSetPosition = 0;//Item 0: all images

    Set<ImageItem> mSelectedImages = new LinkedHashSet<>();
    ;

    public List<ImageSet> getImageSets() {
        return mImageSets;
    }

    public List<ImageItem> getImageItemsOfCurrentImageSet() {
        return mImageSets.get(mCurrentSelectedImageSetPosition).imageItems;
    }

    public void setImageSets(List<ImageSet> mImageSets) {
        this.mImageSets = mImageSets;
    }

    public int getCurrentSelectedImageSetPosition() {
        return mCurrentSelectedImageSetPosition;
    }

    public void setCurrentSelectedImageSetPosition(int mCurrentSelectedImageSetPosition) {
        this.mCurrentSelectedImageSetPosition = mCurrentSelectedImageSetPosition;
    }


    public void addSelectedImageItem(int position, ImageItem item) {
        //mSelectedImages.put(position, item);
        if (mSelectedImages != null && item != null && position > -1) {
            mSelectedImages.add(item);
            Log.i(TAG, "=====select:" + item.path);
            notifyImageSelectedChanged(position, item, true);
        }
    }

    public void deleteSelectedImageItem(int position, ImageItem item) {
        if (mSelectedImages != null && item != null && position > -1) {
            mSelectedImages.remove(item);
            Log.i(TAG, "=====cancel select:" + item.path);
            notifyImageSelectedChanged(position, item, false);
        }
    }

    public boolean isSelect(int position, ImageItem item) {
        if (item == null) {
            return false;
        }
        if (mSelectedImages.contains(item)) {
            return true;
        }
        return false;
    }

    public int getSelectImageCount() {
        if (mSelectedImages == null) {
            return 0;
        }
        return mSelectedImages.size();
    }

    public void clear() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
        if (mImageCropCompleteListeners != null) {
            mImageCropCompleteListeners.clear();
            mImageCropCompleteListeners = null;
        }

        //mSelectedImages.clear();
        //mSelectedImages = null;

        if (mImageSets != null) {
            mImageSets.clear();
            mImageSets = null;
        }

        mCurrentSelectedImageSetPosition = 0;

        Log.i(TAG, "=====destroy:clear all data and listeners");
    }

    public List<ImageItem> getSelectedImages() {
        List<ImageItem> list = new ArrayList<>();
        list.addAll(mSelectedImages);
        return list;
    }

    public void deleteSelectedImages(int postion) {
        if (mSelectedImages != null && mSelectedImages.size() > 0) {
            mSelectedImages.remove(postion);
            Log.i(TAG, "=====clear all selected images");
        }
    }

    public void clearSelectedImages() {
        if (mSelectedImages != null) {
            mSelectedImages.clear();
            Log.i(TAG, "=====clear all selected images");
        }
    }


    public static Bitmap makeCropBitmap(Bitmap bitmap, Rect rectBox, RectF imageMatrixRect, int expectSize) {
        Bitmap bmp = bitmap;
        RectF localRectF = imageMatrixRect;
        float f = localRectF.width() / bmp.getWidth();
        int left = (int) ((rectBox.left - localRectF.left) / f);
        int top = (int) ((rectBox.top - localRectF.top) / f);
        int width = (int) (rectBox.width() / f);
        int height = (int) (rectBox.height() / f);

        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }

        if (left + width > bmp.getWidth()) {
            width = bmp.getWidth() - left;
        }
        if (top + height > bmp.getHeight()) {
            height = bmp.getHeight() - top;
        }

        int k = width;
        if (width < expectSize) {
            k = expectSize;
        }
        if (width > expectSize) {
            k = expectSize;
        }
        try {
            bmp = Bitmap.createBitmap(bmp, left, top, width, height);

            if (k != width && k != height) {//don't do this if equals
                bmp = Bitmap.createScaledBitmap(bmp, k, k, true);//scale the bitmap
            }

        } catch (OutOfMemoryError localOutOfMemoryError1) {
            Log.v(TAG, "OOM when create bitmap");
        }

        return bmp;
    }

    /**
     * create a file to save photo
     *
     * @param ctx
     * @return
     */
    private File createImageSaveFile(Context ctx) {
        if (Util.isStorageEnable()) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!pic.exists()) {
                pic.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "IMG_" + timeStamp;
            File tmpFile = new File(pic, fileName + ".jpg");
            mCurrentPhotoPath = tmpFile.getAbsolutePath();
            Log.i(TAG, "=====camera path:" + mCurrentPhotoPath);
            return tmpFile;
        } else {
            //File cacheDir = ctx.getCacheDir();
            File cacheDir = Environment.getDataDirectory();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "IMG_" + timeStamp;
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            mCurrentPhotoPath = tmpFile.getAbsolutePath();
            Log.i(TAG, "=====camera path:" + mCurrentPhotoPath);
            return tmpFile;
        }
    }


    /**
     * take picture
     */
    public void takePicture(Context ctx, int requestCode) throws IOException {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(ctx.getPackageManager()) != null) {
            // Create the File where the photo should go
            //File photoFile = createImageFile();
            File photoFile = createImageSaveFile(ctx);
            // Continue only if the File was successfully created

            Uri uri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(photoFile);
            } else {
                //通过FileProvider创建一个content类型的Uri(android 7.0需要)
                uri = FileProvider.getUriForFile(ctx, "com.buxingzhe.pedestrian", photoFile);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件

            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        if (ctx instanceof Activity) {
            ((Activity) ctx).startActivityForResult(takePictureIntent, requestCode);
        }

    }

    /**
     * take picture
     */
    public void takePicture(Fragment fragment, int requestCode) throws IOException {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            //File photoFile = createImageFile();
            File photoFile = createImageSaveFile(fragment.getActivity());
            // Continue only if the File was successfully created

            Uri uri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(photoFile);
            } else {
                //通过FileProvider创建一个content类型的Uri(android 7.0需要)
                uri = FileProvider.getUriForFile(fragment.getActivity(), "com.buxingzhe.pedestrian", photoFile);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件

            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }

        }
        fragment.startActivityForResult(takePictureIntent, requestCode);

    }

    /**
     * scan the photo so that the gallery can read it
     *
     * @param ctx
     * @param path
     */
    public static void galleryAddPic(Context ctx, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
        Log.i(TAG, "=====MediaScan:" + path);
    }

    /**
     * listener for one Image Item selected observe
     */
    public interface OnImageSelectedListener {
        void onImageSelected(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit);
    }


    public interface OnImageCropCompleteListener {
        void onImageCropComplete(Bitmap bmp, float ratio);
    }

    public interface OnPictureTakeCompleteListener {
        void onPictureTakeComplete(String picturePath);
    }

    public interface OnImagePickCompleteListener {
        void onImagePickComplete(List<ImageItem> items);
    }

    public interface OnImagePageSelectedListener {
        void onImagePageSelected(List<ImageItem> items);
    }

    public interface Select_Mode {
        int MODE_SINGLE = 0;
        int MODE_MULTI = 1;
    }

}
