package com.lyl.myallforyou.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.SPUtil;

/**
 * Created by lyl on 2017/4/5.
 */

public class BaseActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getName();

    protected Context mContext;
    protected DrawerLayout mDrawerLayout;

    protected String uuid;
    protected String objId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initData();
    }


    private void initData() {
        uuid = (String) SPUtil.get(mContext, Constans.SP_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = AppUtils.getUUID();
            SPUtil.put(mContext, Constans.SP_UUID, uuid);
        }

        objId = (String) SPUtil.get(mContext, Constans.SP_OBJ_ID, "");
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void showT(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }


    public void showT(int r) {
        Toast.makeText(mContext, r, Toast.LENGTH_SHORT).show();
    }


    /**
     * 防止 String 为 null
     *
     * @param s
     * @return
     */
    public String FS(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        } else {
            return s;
        }
    }
}
