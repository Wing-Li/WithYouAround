package com.lyl.myallforyou.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.lyl.myallforyou.MyShared;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.DeviceInfo;
import com.lyl.myallforyou.network.imp.DeviceInfoImp;
import com.lyl.myallforyou.ui.deviceinfo.DeviceInfoActivity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by lyl on 2017/8/4.
 */

public class WidgetProvider extends AppWidgetProvider {
    // 启动ExampleAppWidgetService服务对应的action
    private final Intent EXAMPLE_SERVICE_INTENT = new Intent();
    // 更新 widget 的广播对应的action
    private final String ACTION_UPDATE_ALL = "com.lyl.myallforyou.widget.UPDATE_ALL";
    public static final String CLICK_ACTION = "com.lyl.myallforyou.action.CLICK"; // 点击事件的广播ACTION

    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();

    /**
     * 当小部件被添加时或者每次小部件更新时都会调用一次该方法，配置文件中配置小部件的更新周期 updatePeriodMillis，每次更新都会调用。
     * ACTION_APPWIDGET_UPDATE 和 ACTION_APPWIDGET_RESTORED 。
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();

        if (ACTION_UPDATE_ALL.equals(action)) {
            // “更新”广播
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        } else if (CLICK_ACTION.equals(intent.getAction())) {

        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {

        }
    }

    // 更新所有的 widget
    private void updateAllAppWidgets(final Context context, final AppWidgetManager appWidgetManager, Set set) {
        // widget 的id
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();

        while (it.hasNext()) {
            appID = ((Integer) it.next()).intValue();

            final MyShared myShared = new MyShared(context);
            final String widgetUuid = myShared.getWidgetUuid();
            final int finalAppID = appID;
            DeviceInfoImp.getDeviceInfo(widgetUuid, new DeviceInfoImp.DeviceInfoCallback() {
                @Override
                public void onSuccess(DeviceInfo deviceInfo) {
                    // 获取 example_appwidget.xml 对应的RemoteViews
                    RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                    // 设备厂商
                    remoteView.setTextViewText(R.id.device_manufacturer, deviceInfo.getDevice_manufacturer());
                    // 系统时间
                    remoteView.setTextViewText(R.id.system_time, deviceInfo.getSystem_date() + " " + deviceInfo
                            .getSystem_time());
                    // 电量
                    remoteView.setTextViewText(R.id.system_battery, deviceInfo.getSystem_battery());
                    // 屏幕状态
                    int screen = "true".equals(deviceInfo.getScreen_status()) ? R.string.screen_status_open : R
                            .string.screen_status_close;
                    remoteView.setTextViewText(R.id.screen_status, context.getString(screen));
                    // 位置
                    remoteView.setTextViewText(R.id.address, deviceInfo.getMy_address());
                    // 点击跳转到详情
                    remoteView.setOnClickPendingIntent(R.id.widget_layout, getPendingIntent(context));
                    // 更新 widget
                    appWidgetManager.updateAppWidget(finalAppID, remoteView);
                }

                @Override
                public void onFail(Exception e) {
                }
            });
        }
    }

    private PendingIntent getPendingIntent(Context context) {
        MyShared myShared = new MyShared(context);
        Intent i = new Intent(context, DeviceInfoActivity.class);
        i.putExtra(ConstantIntent.USER_INFO, myShared.getWidgetUuid());
        i.putExtra(ConstantIntent.USER_NAME, myShared.getWidgetName());
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        return pi;
    }

    /**
     * 每删除一次窗口小部件就调用一次
     * ACTION_APPWIDGET_DELETED
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     * ACTION_APPWIDGET_ENABLE
     */
    @Override
    public void onEnabled(Context context) {
        // 在第一个 widget 被创建时，开启服务
        EXAMPLE_SERVICE_INTENT.setAction("android.appwidget.action.APP_WIDGET_SERVICE");
        EXAMPLE_SERVICE_INTENT.setPackage(context.getPackageName());
        context.startService(EXAMPLE_SERVICE_INTENT);

        super.onEnabled(context);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     * ACTION_APPWIDGET_DISABLED
     */
    @Override
    public void onDisabled(Context context) {
        // 在最后一个 widget 被删除时，终止服务
        EXAMPLE_SERVICE_INTENT.setAction("android.appwidget.action.APP_WIDGET_SERVICE");
        EXAMPLE_SERVICE_INTENT.setPackage(context.getPackageName());
        context.stopService(EXAMPLE_SERVICE_INTENT);

        super.onDisabled(context);
    }

    /**
     * 当小部件大小改变时
     * ACTION_APPWIDGET_OPTIONS_CHANGED
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle
            newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     * ACTION_APPWIDGET_RESTORED
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
