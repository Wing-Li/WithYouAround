package com.lyl.myallforyou.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.ui.main.MainActivity;
import com.lyl.myallforyou.utils.DeviceStatusUtils;
import com.lyl.myallforyou.utils.NetUtil;
import com.lyl.myallforyou.utils.SPUtil;


public class SplashActivity extends BaseActivity {

    /**
     * 页面等待时间
     */
    private final static long WAIT_TIME = 1000;

    private long mStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartTime = System.currentTimeMillis();
        setContentView(R.layout.activity_splash);

        if ("VirtualBox".equals(DeviceStatusUtils.getModel())) {
            Toast.makeText(getApplicationContext(), "本程序不支持模拟器操作", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setAnimation();
        checkNet();
    }

    private void checkNet() {
        if (TextUtils.isEmpty(objId) && !NetUtil.isNetworkAvailable(mContext)) {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext)//
                    .setTitle(R.string.hint)//
                    .setMessage(R.string.init_data_not_net_text)//
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            dialogInterface.dismiss();
                        }
                    })//
                    .setPositiveButton(R.string.had, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (NetUtil.isNetworkAvailable(mContext)) {
                                dialogInterface.dismiss();
                                initUserInfo();
                            } else {
                                showT(getString(R.string.two_not_net));
                                finish();
                            }
                        }
                    }).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            initUserInfo();
        }
    }


    /**
     * 初始化我的信息
     */
    private void initUserInfo() {
        AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
        query.whereContains(Constans.USER_MYID, uuid);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null && i <= 0) {
                    final AVObject userInfo = new AVObject(Constans.TABLE_USER_INFO);
                    userInfo.put(Constans.USER_MYID, uuid);
                    userInfo.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                Toast.makeText(mContext, R.string.upload_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, R.string.upload_success, Toast.LENGTH_SHORT).show();
                                String objectId = userInfo.getObjectId();
                                SPUtil.put(mContext, Constans.SP_OBJ_ID, objectId);
                            }
                        }
                    });
                }
                initMain();
            }
        });
    }


    private void initMain() {
        // 如果加载时间不到2秒就等一会，超过两秒就直接跳
        long ringTime = System.currentTimeMillis() - mStartTime;
        if (ringTime < WAIT_TIME) {
            try {
                Thread.sleep(WAIT_TIME - ringTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void setAnimation() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(WAIT_TIME);
        translate.setDuration(WAIT_TIME);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translate);
        animationSet.addAnimation(alpha);
        layout.startAnimation(translate);
    }
}
