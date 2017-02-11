package com.github.bigexcalibur.herovideo.util;

import android.util.Log;

/**
 * 日志打印工具类
 */
public class LogUtil {

    private static final String TAG = "LogUtil";

    private static boolean isShow = true;

    public static boolean isShow() {

        return isShow;
    }

    public static void setShow(boolean show) {

        isShow = show;
    }

    public static void i(String tag, String msg) {

        if (isShow)
            Log.i(tag + "_", msg);
    }

    public static void w(String tag, String msg) {

        if (isShow)
            Log.w(tag + "_", msg);
    }

    public static void d(String tag, String msg) {

        if (isShow)
            Log.d(tag + "_", msg);
    }

    public static void e(String tag, String msg) {

        if (isShow)
            Log.e(tag + "_", msg);
    }

    public static void all(String msg) {

        if (isShow)
            Log.e("all", msg);
    }

    public static void i(String msg) {

        if (isShow)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {

        if (isShow)
            Log.w(TAG, msg);
    }

    public static void e(String msg) {

        if (isShow)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        e(msg);
    }

    public static void d(String msg) {
        v(msg);
    }

    public static void test(String msg){
        if (isShow)
            Log.d("test_",msg);
    }
}
