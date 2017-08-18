package com.lyl.myallforyou.widget;

import android.content.Context;
import android.content.Intent;

import com.lyl.myallforyou.utils.ServiceUtils;
import com.tencent.bugly.beta.download.BetaReceiver;

/**
 * Created by lyl on 2017/8/18.
 */

public class WidgetReceiver extends BetaReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean appwidget = ServiceUtils.isServiceRunning(context.getApplicationContext(), "android.appwidget.action.APP_WIDGET_SERVICE");
        if (!appwidget) {
            ServiceUtils.startService(context.getApplicationContext(), WidgetService.class);
        }
    }
}
