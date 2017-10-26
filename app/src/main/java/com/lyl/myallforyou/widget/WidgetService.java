package com.lyl.myallforyou.widget;

import android.app.Service;
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

    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // 中断线程，即结束线程。
        if (mTimerTask !=null){
            mTimerTask.cancel();
            mTimerTask = null;
        }

        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    * 服务开始时，即调用startService()时，onStartCommand()被执行。
    * START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service
       ，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
    */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
                sendBroadcast(updateIntent);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 4000, MyApp.UPLOAD_SPACE_TIME);

        return super.onStartCommand(intent, flags, startId);
    }
}
