package com.lyl.myallforyou;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.avos.avoscloud.AVOSCloud;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.im.entity.NotificationClickEventReceiver;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.utils.SPUtil;
import com.tencent.bugly.Bugly;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by lyl on 2017/4/6.
 */

public class MyApp extends Application {

    public static final String TARGET_ID = "targetId";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static final String GROUP_ID = "groupId";
    public static final String CONV_TITLE = "conv_title";

    /**
     * 位置获取的间隔时间
     */
    public static int MAP_SPACE_TIME = 5 * 60 * 1000;
    /**
     * 信息上传的间隔时间
     */
    public static int UPLOAD_SPACE_TIME = 10 * 60 * 1000 + 2000;

    public static String mAppShare = "http://www.coolapk.com/apk/com.lyl.myallforyou";
    public static LiteOrm liteOrm;
    private static String appPath;

    @Override
    public void onCreate() {
        super.onCreate();

        initLeancloud();

        initSQL();

        initBugly();

        initJG();

        initSpaceTime();
    }

    private void initJG(){
        JMessageClient.init(this);
        //设置Notification的模式
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(getApplicationContext());
    }

    private void initSpaceTime() {
        UPLOAD_SPACE_TIME = (int) SPUtil.get(this, Constans.SP_UPLOAD_SPACE_TIME, UPLOAD_SPACE_TIME);
    }

    public static String getAppPath() {
        if (!TextUtils.isEmpty(appPath)) {
            return appPath;
        }
        File sdFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        File my = new File(sdFile, "ForYou");
        if (!my.exists()) {
            my.mkdirs();
        }
        return appPath = my.getAbsolutePath();
    }

    private void initBugly() {
        if (MyUtils.isDev()) {
            Bugly.init(getApplicationContext(), BuildConfig.BuglyAppId, true);
        } else {
            Bugly.init(getApplicationContext(), BuildConfig.BuglyAppId, false);
        }
    }

    private void initLeancloud() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, BuildConfig.AVOSCloudAppId, BuildConfig.AVOSCloudAppKey);
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
    }

    private void initSQL() {
        if (liteOrm == null) {
            DataBaseConfig config = new DataBaseConfig(this, "foryou.db");
            config.dbVersion = 1;
            config.onUpdateListener = null;
            liteOrm = LiteOrm.newSingleInstance(config);
        }
        liteOrm.setDebugged(true);
    }
}
