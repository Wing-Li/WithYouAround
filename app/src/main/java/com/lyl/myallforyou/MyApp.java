package com.lyl.myallforyou;

import android.os.Environment;
import android.text.TextUtils;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.utils.SPUtil;

import java.io.File;

import androidx.multidex.MultiDexApplication;

/**
 * Created by lyl on 2017/4/6.
 */

public class MyApp extends MultiDexApplication {

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

        initSQL();

        initSpaceTime();
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
