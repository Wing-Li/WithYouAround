package com.lyl.myallforyou.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lyl.myallforyou.utils.ServiceUtils;

/**
 * Created by lyl on 2017/8/18.
 */

public class WidgetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 检查 上传服务/更新桌面部件服务，没开，就打开
        ServiceUtils.startMyServer(context);
    }
}
