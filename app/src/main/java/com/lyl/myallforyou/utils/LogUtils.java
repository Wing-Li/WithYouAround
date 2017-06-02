package com.lyl.myallforyou.utils;

import android.util.Log;

import com.lyl.myallforyou.BuildConfig;

/**
 * @author LX
 * @ClassName LogUtils
 * @Description Debug调试工具类
 * @date 2015年1月9日 下午5:29:55
 */
public class LogUtils {

    private static final String TAG = "bnzy";

    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /*
     * 是否需要打印bug，可以在application的onCreate函数里面初始化
     */
    public static boolean isDebug = true;

    static {
        if ("dev".equals(BuildConfig.Environment)) {
            isDebug = true;
        } else {
            isDebug = false;
        }
    }

	/*
     * 下面四个是默认tag的函数
	 */

    public static void i(String msg) {
        if (isDebug && msg != null) Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug && msg != null) Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug && msg != null) Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug && msg != null) Log.v(TAG, msg);
    }

	/*
     * 下面是传入自定义tag的函数
	 */

    public static void i(String tag, String msg) {
        if (isDebug && msg != null) Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug && msg != null) Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug && msg != null) Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug && msg != null) Log.v(tag, msg);
    }
}
