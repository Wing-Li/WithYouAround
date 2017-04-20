package com.lyl.myallforyou;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.tencent.bugly.Bugly;

/**
 * Created by lyl on 2017/4/6.
 */

public class MyApp extends Application {

    public static int SPACE_TIME = 5 * 60 * 1000 + 2000;

    public static LiteOrm liteOrm;


    @Override
    public void onCreate() {
        super.onCreate();

        initLeancloud();

        initSQL();

        initBugly();
    }


    private void initBugly() {
        Bugly.init(getApplicationContext(), "9cbcfc1af7", true);
    }


    private void initLeancloud() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"zvD19ojtYQcAW7KijUr0onbD-gzGzoHsz","B1JhfQQCfYrH8f3e1RuhbrXc");
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
