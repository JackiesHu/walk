package com.buxingzhe.pedestrian.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * 上传图片类，选择照片或拍照裁剪图片大小等
 */

public class PicUtil {

    /**
     * 选择拍照的request_code
     */
    public static final int TAKEPHOTO_PIC_CODE = 0;
    /**
     * 选择从图库选择图片的request_code
     */
    public static final int SELECT_PIC_CODE = 1;

    /**
     * 裁剪图片的request_code
     */
    public static final int CUT_PIC_CODE = 2;

    // 调用相机拍摄照片后保存的路径和名字

    public static String takePicturePath = null;
    public static String cutPicPath;
    /**
     * 拍照后图片保存路径
     */

    public static void showPicDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("请选择操作")
                .setItems(new String[]{"拍照", "从图库选择"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    // 拍照
                                    case TAKEPHOTO_PIC_CODE:
                                        takePhoto(activity);
                                        break;
                                    // 选择图片
                                    case SELECT_PIC_CODE:
                                        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                                        intent1.setDataAndType(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                "image/*");
                                        activity.startActivityForResult(intent1, SELECT_PIC_CODE);
                                        break;
                                }
                            }
                        }).show();

    }

    /**
     * 拍照后保存图片
     */
    private static void takePhoto(Activity activity) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(activity, "未检测到内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        File potosF = new File(FileConfig.IMAGE_UP_PATH);
        if (!potosF.exists()) {
            potosF.mkdirs();
        }
        takePicturePath = FileConfig.IMAGE_UP_PATH + System.currentTimeMillis() + ".jpg";
        File image = new File(takePicturePath);
        Uri uri=null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(image);
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要)
            uri = FileProvider.getUriForFile(activity, "com.buxingzhe.pedestrian", image);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件

        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, TAKEPHOTO_PIC_CODE);
    }

    /**
     * 裁剪图片
     */
    public static void cutPhotoZoom(Intent data, Activity activity) {
        String picPath = null;
        // data==null，表示选择拍照，拍照后返回的data为null，不能从data获取到拍照的图片路径，故只有takePicturePath记录路径
        if (null == data) {
            picPath = takePicturePath;
        } else {// 选择图库，可以从data获取到路径
            picPath = getSelectPicPath(data, activity);
        }
        if (picPath == null) {
            return;
        }



        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0通过FileProvider授权访问
            uri = FileProvider.getUriForFile(activity, "com.buxingzhe.pedestrian", new File(picPath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件

        } else {
            uri = Uri.fromFile(new File(picPath));
        }
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        // 保存裁剪图片路径
        String path = FileConfig.IMAGE_UP_PATH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 裁剪后图片保存地址
        cutPicPath = path + System.currentTimeMillis() + ".jpg";
        intent.putExtra("output", uri);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CUT_PIC_CODE);
    }

    /**
     * 选择图片后得到图片的路径
     *
     * @param data
     * @param activity
     * @return
     */

    public static String getSelectPicPath(Intent data, Activity activity) {

        Uri uri = data.getData();
        String[] projection = {MediaStore.Images.Media.DATA};
        ContentResolver mContentResolver = activity.getContentResolver();
        Cursor cursor = mContentResolver.query(uri, projection, null, null,
                null);
        int columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String picPath = cursor.getString(columnIndex);

        return picPath;
    }

    /**
     * 选择图片后得到图片的路径,再根据图片路径和给出宽，高或得裁剪的Bitmap
     *
     * @param data
     * @param activity
     */
    public static Bitmap getBitmapByPicPath(Intent data,
                                            final Activity activity, final int viewWidth, final int viewHeight) {
        Bitmap bitmap = null;
        String picPath = null;
        // data==null，表示选择拍照，拍照后返回的data为null，不能从data获取到拍照的图片路径，故只有takePicturePath记录路径
        if (null == data) {
            picPath = takePicturePath;
        } else {// 选择图库，可以从data获取到路径
            picPath = getSelectPicPath(data, activity);
        }
        bitmap = decodeThumbBitmapForFile(picPath, viewWidth, viewHeight);

        return bitmap;
    }

    /**
     * 根据控件高宽获取bitmap，避免图片过大，浪费内存
     *
     * @param path       ---图片本地路径
     * @param viewWidth
     * @param viewHeight
     * @return
     */

    public static Bitmap decodeThumbBitmapForFile(String path, int viewWidth,
                                                  int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        // 设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     *
     * @param options
     * @param viewWidth
     * @param viewHeight
     */
    private static int computeScale(BitmapFactory.Options options,
                                    int viewWidth, int viewHeight) {
        int inSampleSize = 1;
        if (viewWidth == 0 || viewHeight == 0) {
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        // 假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
            int widthScale = Math
                    .round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight
                    / (float) viewWidth);

            // 为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }
}
