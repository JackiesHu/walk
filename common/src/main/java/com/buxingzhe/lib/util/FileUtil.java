package com.buxingzhe.lib.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by zhaishaoping on 29/03/2017.
 */

public class FileUtil {
    private FileUtil() {
    }

    public static File getCacheDir(Context context,String dirName) {
        File result;
        if(existsSdcard().booleanValue()) {
            File cacheDir = context.getExternalCacheDir();
            if(cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            result = new File(context.getCacheDir(), dirName);
        }

        return !result.exists() && !result.mkdirs()?null:result;
    }

    public static boolean isDiskAvailable() {
        long size = getDiskAvailableSize();
        return size > 10485760L;
    }

    public static long getDiskAvailableSize() {
        if(!existsSdcard().booleanValue()) {
            return 0L;
        } else {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getAbsolutePath());
            long blockSize = (long)stat.getBlockSize();
            long availableBlocks = (long)stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }
    }

    public static Boolean existsSdcard() {
        return Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
    }

    public static long getFileOrDirSize(File file) {
        if(!file.exists()) {
            return 0L;
        } else if(!file.isDirectory()) {
            return file.length();
        } else {
            long length = 0L;
            File[] list = file.listFiles();
            if(list != null) {
                File[] var4 = list;
                int var5 = list.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File item = var4[var6];
                    length += getFileOrDirSize(item);
                }
            }

            return length;
        }
    }

    public static boolean copy(String fromPath, String toPath) {
        boolean result = false;
        File from = new File(fromPath);
        if(!from.exists()) {
            return result;
        } else {
            File toFile = new File(toPath);
            IOUtil.deleteFileOrDir(toFile);
            File toDir = toFile.getParentFile();
            if(toDir.exists() || toDir.mkdirs()) {
                FileInputStream in = null;
                FileOutputStream out = null;

                try {
                    in = new FileInputStream(from);
                    out = new FileOutputStream(toFile);
                    IOUtil.copy(in, out);
                    result = true;
                } catch (Throwable var12) {
                    LogUtil.d(var12.getMessage(), var12);
                    result = false;
                } finally {
                    IOUtil.closeQuietly(in);
                    IOUtil.closeQuietly(out);
                }
            }

            return result;
        }
    }
}
