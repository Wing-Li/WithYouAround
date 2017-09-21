package com.lyl.myallforyou.service;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.lyl.myallforyou.utils.ServiceUtils;

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
        // 检查 上传服务/更新桌面部件服务，没开，就打开
        ServiceUtils.startMyServer(getApplicationContext());
    }

}
