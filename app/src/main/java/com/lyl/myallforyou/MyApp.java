package com.lyl.myallforyou;

import android.app.Application;

import com.litesuits.orm.LiteOrm;

/**
 * Created by lyl on 2017/4/6.
 */

public class MyApp extends Application {

    public static LiteOrm liteOrm;


    @Override
    public void onCreate() {
        super.onCreate();

        initSQL();
    }


    private void initSQL() {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(this, "foryou.db");
        }
        liteOrm.setDebugged(true);
    }
}
