package com.buxingzhe.pedestrian.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PictureUtil {
    public static Bitmap getZoomImage(String srcPath,int degree) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inPurgeable=true;
        newOpts.inInputShareable = true;
        newOpts.inJustDecodeBounds = true;
        //Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float ww = 720;//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例

        newOpts.inTempStorage= new byte[12 * 1024];
        File file = new File(srcPath);
        FileInputStream fs = null;
        Bitmap bmp = null;
        try {
            fs = new FileInputStream(file);
            if(fs != null)
                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        return  bitmap;

        if(!bmp.isRecycled() ){
            System.gc();
        }
        return newcompressImage(bmp, degree, bmp.getWidth(), bmp.getHeight());//压缩好比例大小后再进行质量压缩
    }
    public static Bitmap newcompressImage(Bitmap bitmap,int degree,int width,int height){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap nbitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,height, matrix, true);
        return nbitmap;
    }
    public static String createTempFile(Bitmap bitmap) throws IOException{
//        Bitmap bitmapOrg = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        if(bitmap == null)
            return null;
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"yd_temp.jpg");
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out))
                out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /* 旋转图片
    * @param angle
    * @param bitmap
    * @return Bitmap
    */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static Bitmap getimage(String srcPath,int degree) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inPurgeable=true;
        newOpts.inInputShareable = true;
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float ww = 640;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        }
        if(be>=5&&be<8){
            be=5;
        }else if(be>=8&&be<=10){
            be=8;
        }else if(be>10){
            be=10;
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        System.gc();
        return newcompressImage(bitmap, degree, bitmap.getWidth(), bitmap.getHeight());//压缩好比例大小后再进行质量压缩
    }
}
