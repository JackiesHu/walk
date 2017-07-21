package com.pizidea.imagepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * @Description：TODO(- 类描述：文件操作类 -)
 * @author：wsx
 * @email：heikepianzi@qq.com
 * @date 2015/09/07 14:59
 */
public class FileUtil {

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * -- 获取sd卡路径 双sd卡时，根据”设置“里面的数据存储位置选择，获得的是内置sd卡或外置sd卡
     *
     * @return
     */
    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static File getSmallBitmap(Context context, String fileSrc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileSrc, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        Bitmap img = BitmapFactory.decodeFile(fileSrc, options);
        String filename = context.getFilesDir()+ File.separator + "video-" + img.hashCode() + ".jpg";
        saveBitmap2File(img, filename);
        return new File(filename);

    }
    /**
     * 保存bitmap到文件
     *
     * @param bmp
     * @param filename
     * @return
     */
    public static boolean saveBitmap2File(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 50;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    /**
     * 设置压缩的图片的大小设置的参数
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(height) / reqHeight;
            int widthRatio = Math.round(width) / reqWidth;
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * -- 复制文件
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists())  //文件不存在时
                oldfile.mkdirs();
            InputStream inStream = new FileInputStream(oldPath); //读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            fs.close();
            inStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static double getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        if (file.exists()) {
            try {
                if (file.isDirectory()) {
                    blockSize = getFileSizes(file);
                } else {
                    blockSize = getFileSize(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("获取文件大小", "获取失败!");
            }
        }
        return blockSize;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.##");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.##");
        String fileSize = "";
        if (fileS < 1024)
            fileSize = Double.valueOf(df.format((double) fileS)) + "B";
        else if (fileS < 1024 * 1024)
            fileSize = Double.valueOf(df.format((double) fileS / 1024)) + "KB";
        else if (fileS < 1024 * 1024 * 1024)
            fileSize = Double.valueOf(df.format((double) fileS / (1024 * 1024))) + "MB";
        else if (fileS < 1024 * 1024 * 1024 * 1024)
            fileSize = Double.valueOf(df.format((double) fileS / (1024 * 1024 * 1024))) + "GB";
        return fileSize;
    }

    /**
     * -- 压缩图片质量和尺寸
     *
     * @param path
     * @throws IOException
     */
    public static boolean compressFile(String path) {

        // 现在主流手机比较多是1280*720分辨率，所以默认高和宽我们设置
        return compressFile(path, 1280, 720);
    }

    /**
     * -- 压缩图片质量和尺寸
     *
     * @param path
     * @param height
     * @param wight
     * @return
     */
    public static boolean compressFile(String path, float height, float wight) {

        Bitmap bitmap = getBitmap(path, height, wight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;  // -设置压缩质量
        int fileSize = 100; // -设置压缩大小（kb）
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length / 1024 > fileSize) {
            baos.reset();
            quality -= 10;
            if (quality < 50) {  // 压缩质量不能低于50
                fileSize += 50;
                quality = 100;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        File f = new File(path);
        if (f.exists())
            f.delete();
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * -- 获取sd卡图片位图
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {
        return getBitmap(path, 1280, 7200);
    }

    /**
     * -- 获取sd卡图片位图
     *
     * @param path
     * @param height
     * @param wight
     * @return
     */
    public static Bitmap getBitmap(String path, float height, float wight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int w = options.outWidth;
        int h = options.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > wight || h > height) {
            if (w > h && w > wight) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int) (options.outWidth / wight);
            } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
                be = (int) (options.outHeight / height);
            }
        }
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;// 设置缩放比例
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 位图存为图片
     *
     * @param path
     * @param mBitmap
     */
    public static void saveBitmap(String path, Bitmap mBitmap) {

        try {
            File f = new File(path);
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            FileOutputStream fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e("TAG", "delete file no exists " + file.getAbsolutePath());
        }
    }

    /**
     * ----图片压缩处理
     *
     * @param oldFilePath
     * @param newFilePath
     * @throws IOException
     */
    public static void compressBmpToFile(String oldFilePath, String newFilePath) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(oldFilePath, options);
        // 获取到这个图片的原始宽度和高度
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        options.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        // if (picWidth > picHeight) {
        // if (picWidth > screenWidth)
        // options.inSampleSize = picWidth / screenWidth;
        // } else {
        // if (picHeight > screenHeight)
        // options.inSampleSize = picHeight / screenHeight;
        // }
        // Log.v("---display---",
        // String.valueOf(screenWidth)+"---"+String.valueOf(screenHeight));
        if (picHeight > 1920 || picWidth > 1080) {
            final int heightRatio = Math.round((float) picHeight / (float) 800);
            final int widthRatio = Math.round((float) picWidth / (float) 480);
            options.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        options.inJustDecodeBounds = false;
        System.out.println("oldFilePath"+oldFilePath);
        bitmap = BitmapFactory.decodeFile(oldFilePath, options);
        System.out.println("---2"+bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100; // 个人喜欢从80开始,
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length / 1024 > 300) {
            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        FileOutputStream fos = new FileOutputStream(new File(newFilePath));
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
    }
}
