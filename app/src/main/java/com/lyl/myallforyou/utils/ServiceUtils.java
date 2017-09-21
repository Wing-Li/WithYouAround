package com.lyl.myallforyou.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.lyl.myallforyou.service.DeviceInfoService;
import com.lyl.myallforyou.widget.WidgetService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lyl on 2017/5/27.
 */

public final class ServiceUtils {

    private ServiceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static final String DEVICE_INFO_SERVICE = "com.lyl.myallforyou.service.DeviceInfoService";
    public static final String WIDGET_SERVICE = "com.lyl.myallforyou.widget.WidgetService";

    public static void startMyServer(Context context) {
        boolean serviceRunning = ServiceUtils.isServiceRunning(context.getApplicationContext(), DEVICE_INFO_SERVICE);
        if (!serviceRunning) {
            ServiceUtils.startService(context.getApplicationContext(), DeviceInfoService.class);
        }
//        boolean isAppWidgetExist = (boolean) SPUtil.get(context, Constans.SP_WIDGET_IS_EXIST, false);
//        if (isAppWidgetExist) {
            boolean appwidget = ServiceUtils.isServiceRunning(context.getApplicationContext(), WIDGET_SERVICE);
            if (!appwidget) {
                ServiceUtils.startService(context.getApplicationContext(), WidgetService.class);
            }
//        }
    }

    /**
     * 获取所有运行的服务
     *
     * @return 服务名集合
     */
    public static Set getAllRunningService(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
        Set<String> names = new HashSet<>();
        if (info == null || info.size() == 0) return null;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            names.add(aInfo.service.getClassName());
        }
        return names;
    }

    /**
     * 启动服务
     *
     * @param className 完整包名的服务类名
     */
    public static void startService(Context context, String className) {
        try {
            startService(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务
     *
     * @param cls 服务类
     */
    public static void startService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startService(intent);
    }

    /**
     * 停止服务
     *
     * @param className 完整包名的服务类名
     * @return {@code true}: 停止成功<br>{@code false}: 停止失败
     */
    public static boolean stopService(Context context, String className) {
        try {
            return stopService(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 停止服务
     *
     * @param cls 服务类
     * @return {@code true}: 停止成功<br>{@code false}: 停止失败
     */
    public static boolean stopService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        return context.stopService(intent);
    }

    /**
     * 绑定服务
     *
     * @param className 完整包名的服务类名
     * @param conn      服务连接对象
     * @param flags     绑定选项
     *                  <ul>
     *                  <li>{@link Context#BIND_AUTO_CREATE}</li>
     *                  <li>{@link Context#BIND_DEBUG_UNBIND}</li>
     *                  <li>{@link Context#BIND_NOT_FOREGROUND}</li>
     *                  <li>{@link Context#BIND_ABOVE_CLIENT}</li>
     *                  <li>{@link Context#BIND_ALLOW_OOM_MANAGEMENT}</li>
     *                  <li>{@link Context#BIND_WAIVE_PRIORITY}</li>
     *                  </ul>
     */
    public static void bindService(Context context, String className, ServiceConnection conn, int flags) {
        try {
            bindService(context, Class.forName(className), conn, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定服务
     *
     * @param cls   服务类
     * @param conn  服务连接对象
     * @param flags 绑定选项
     *              <ul>
     *              <li>{@link Context#BIND_AUTO_CREATE}</li>
     *              <li>{@link Context#BIND_DEBUG_UNBIND}</li>
     *              <li>{@link Context#BIND_NOT_FOREGROUND}</li>
     *              <li>{@link Context#BIND_ABOVE_CLIENT}</li>
     *              <li>{@link Context#BIND_ALLOW_OOM_MANAGEMENT}</li>
     *              <li>{@link Context#BIND_WAIVE_PRIORITY}</li>
     *              </ul>
     */
    public static void bindService(Context context, Class<?> cls, ServiceConnection conn, int flags) {
        Intent intent = new Intent(context, cls);
        context.bindService(intent, conn, flags);
    }

    /**
     * 解绑服务
     *
     * @param conn 服务连接对象
     */
    public static void unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    /**
     * 判断服务是否运行
     *
     * @param className 完整包名的服务类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }
}

