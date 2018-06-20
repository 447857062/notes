package kelijun.com.sqlite.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by ${kelijun} on 2018/6/20.
 */

public class Utils {
    private static final String TAG = "BitmapCommonUtils";
    private static final long POLY64REV = 0x95AC9329AC4BC9B5L;
    private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;

    private static long[] sCrcTable = new long[256];

    /**
     * 获取可以使用的缓存目录
     *
     * @param context
     * @param uniqueName 目录名称
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ?
                getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
        return new File(cachePath + File.pathSeparator + uniqueName);
    }

    /**
     * 获取程序外部的缓存目录
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 获取bitmap的字节大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 获取文件路径空间大小
     *
     * @param path
     * @return
     */
    public static long getUsableSpace(File path) {
        try {
            final StatFs statFs = new StatFs(path.getPath());
            return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
        } catch (Exception e) {
            Log.e(TAG, "获取 sdcard 缓存大小 出错，请查看AndroidManifest.xml 是否添加" +
                    "了sdcard的访问权限");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取字符串的byte[]字节数组
     *
     * @param in
     * @return
     */
    public static byte[] getBytes(String in) {
        byte[] result = new byte[in.length() * 2];
        int output = 0;
        for (char ch : in.toCharArray()
                ) {
            result[output++] = (byte) (ch & 0xFF);
            result[output++] = (byte) (ch >> 8);
        }
        return result;
    }

    /**
     * 判断byte[]数组是不是一样的
     *
     * @param key
     * @param buffer
     * @return
     */
    public static boolean isSameKey(byte[] key, byte[] buffer) {
        int n = key.length;
        if (buffer.length < n) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (key[i] != buffer[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拷贝字节数组的其中一段,不需要考虑to-from大于源数组真实长度比拷贝长度短的问题
     *
     * @param original
     * @param from
     * @param to
     * @return
     */
    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + ">" + to);
        }
        byte[] copy = new byte[newLength];
        //拷贝数组,源数组,起始位置,目标数组,目标数组的起始位置,拷贝的长度
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    /**
     * 计算crc64
     */
    static {
        long part;
        for (int i = 0; i < 256; i++) {
            part = i;
            for (int j = 0; j < 8; j++) {
                long x = ((int) part & 1) != 0 ? POLY64REV : 0;
                part = (part >> 1) ^ x;
            }
            sCrcTable[i] = part;
        }
    }

    /**
     * 获取httpurl的byte[]数组
     *
     * @param httpUrl
     * @return
     */
    public static byte[] makeKey(String httpUrl) {
        return getBytes(httpUrl);
    }

    /**
     * 从字符串获取crc64
     * @param in
     * @return
     */
    public static final long crc64Long(String in) {
        if(in==null || in.length()==0){
            return 0;
        }
        return crc64Long(getBytes(in));
    }

    /**
     * 从字节数组获取crc64
     * @param buffer
     * @return
     */
    public static final long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        for (int k = 0, n = buffer.length; k < n; ++k) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }
}
