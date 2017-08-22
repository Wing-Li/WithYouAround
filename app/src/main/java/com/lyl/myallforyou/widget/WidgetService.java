package com.lyl.myallforyou.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lyl.myallforyou.MyApp;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lyl on 2017/8/18.
 */

public class WidgetService extends Service {

    // 更新 widget 的广播对应的action
    public static final String ACTION_UPDATE_ALL = "com.lyl.myallforyou.widget.UPDATE_ALL";

    private Context mContext;

    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public void onCreate() {
        // 创建并开启线程
        mContext = this.getApplicationContext();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
                mContext.sendBroadcast(updateIntent);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 4000, MyApp.UPLOAD_SPACE_TIME);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // 中断线程，即结束线程。
        mTimerTask.cancel();
        mTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    * 服务开始时，即调用startService()时，onStartCommand()被执行。
    */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }
}
