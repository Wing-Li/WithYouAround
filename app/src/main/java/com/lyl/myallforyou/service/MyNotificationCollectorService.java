package com.lyl.myallforyou.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.lyl.myallforyou.utils.ServiceUtils;

/**
 * Created by lyl on 2017/5/27.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyNotificationCollectorService extends NotificationListenerService {

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        checkService();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        checkService();
    }

    private void checkService() {
        boolean serviceRunning = ServiceUtils.isServiceRunning(getApplicationContext(), "com.lyl.myallforyou.service.DeviceInfoService");
        if (!serviceRunning) {
            ServiceUtils.startService(getApplicationContext(), DeviceInfoService.class);
        }
    }

}
