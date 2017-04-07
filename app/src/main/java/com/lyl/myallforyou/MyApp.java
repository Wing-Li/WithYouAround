package com.lyl.myallforyou;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.litesuits.orm.LiteOrm;

/**
 * Created by lyl on 2017/4/6.
 */

public class MyApp extends Application {

    public static LiteOrm liteOrm;


    @Override
    public void onCreate() {
        super.onCreate();

        initLeancloud();

        initSQL();
    }


    private void initLeancloud() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"zvD19ojtYQcAW7KijUr0onbD-gzGzoHsz","B1JhfQQCfYrH8f3e1RuhbrXc");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
    }


    private void initSQL() {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(this, "foryou.db");
        }
        liteOrm.setDebugged(true);
    }
}
