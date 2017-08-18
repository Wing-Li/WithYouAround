package com.lyl.myallforyou;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lyl.myallforyou.utils.SPUtil;

/**
 * Created by lyl on 2017/8/18.
 */

public class MyShared {

    private Context context;
    public SharedPreferences sharedPreferences;

    private static String WidgetUuid = "WidgetUuid";
    private static String WidgetName = "WidgetName";


    public MyShared(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getWidgetUuid() {
        return (String) SPUtil.get(context, WidgetUuid, "");
    }

    public void setWidgetUuid(String w) {
        SPUtil.put(context, WidgetUuid, w);
    }

    public String getWidgetName() {
        return (String) SPUtil.get(context, WidgetName, "");
    }

    public void setWidgetName(String w) {
        SPUtil.put(context, WidgetName, w);
    }
}
