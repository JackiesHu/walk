package com.buxingzhe.pedestrian.utils;

import android.os.Environment;

/**
 * @Description：TODO(- 类描述：文件路径 -)
 * @author：wsx
 * @email：heikepianzi@qq.com
 * @date 2015/09/07 13:58
 */
public class FileConfig {

    static String SDCARDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String APP_PATH = SDCARDPATH + "/buxingzhelianmeng";

    public static String DATA_CACHE_PATH = APP_PATH + "/cache/";// 存放数据缓存文件，如xml数据等
    public static String IMAGE_CACHE_PATH = APP_PATH + "/image/";// 存放图片文件缓存文件
    public static String IMAGE_CACHE_HEADPATH = APP_PATH + "/hendimage/";// 存放头像图片文件缓存文件
    public static String IMAGE_UP_PATH = APP_PATH + "/upimage/";// 存放上传压缩图片文件
    public static String DOWNLOAD_PATH = APP_PATH + "/download/";// 存放下载文件
}
