package com.lyl.myallforyou.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.SPUtil;
import com.lyl.myallforyou.utils.ServiceUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
        initAppData();

        int i = (int) SPUtil.get(getApplicationContext(), Constans.SP_THEME, R.color.colorPrimary);
        setMyTheme(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 检查 上传服务/更新桌面部件服务，没开，就打开
        ServiceUtils.startMyServer(getApplicationContext());
    }

    private void initAppData() {
        uuid = (String) SPUtil.get(mContext, Constans.SP_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = AppUtils.getUUID();
            SPUtil.put(mContext, Constans.SP_UUID, uuid);
        }

        objId = (String) SPUtil.get(mContext, Constans.SP_OBJ_ID, "");
    }


    protected void setBackUI(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
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
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    public void showT(int r) {
        Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
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

    public void setMyTheme(int selectedColor) {
        if (selectedColor == getResources().getColor(R.color.colorBluePrimary)) {
            setTheme(R.style.BlueTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorRedPrimary)) {
            setTheme(R.style.RedTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary)) {
            setTheme(R.style.BrownTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary)) {
            setTheme(R.style.GreenTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary)) {
            setTheme(R.style.PurpleTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorPrimary)) {
            setTheme(R.style.TealTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary)) {
            setTheme(R.style.PinkTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorDeepPurplePrimary)) {
            setTheme(R.style.DeepPurpleTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary)) {
            setTheme(R.style.OrangeTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary)) {
            setTheme(R.style.IndigoTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary)) {
            setTheme(R.style.LightGreenTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary)) {
            setTheme(R.style.DeepOrangeTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorLimePrimary)) {
            setTheme(R.style.LimeTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary)) {
            setTheme(R.style.BlueGreyTheme);
        } else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary)) {
            setTheme(R.style.CyanTheme);
        }
    }
}
