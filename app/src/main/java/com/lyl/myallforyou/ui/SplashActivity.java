package com.lyl.myallforyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.ui.main.MainActivity;
import com.lyl.myallforyou.utils.SPUtil;

import static com.lyl.myallforyou.constants.Constans.USER_MYID;

public class SplashActivity extends BaseActivity {

    /**
     * 页面等待时间
     */
    private final static long WAIT_TIME = 1000;

    private long mStartTime;
    private int what;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartTime = System.currentTimeMillis();
        setContentView(R.layout.activity_splash);

        setAnimation();
        initUserInfo();
    }


    /**
     * 初始化我的信息
     */
    private void initUserInfo() {
        AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
        query.whereContains(USER_MYID, uuid);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null && i <= 0) {
                    final AVObject userInfo = new AVObject(Constans.TABLE_USER_INFO);
                    userInfo.put(USER_MYID, uuid);
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
                handler.sendEmptyMessage(what += 1);
            }
        });

        AVQuery<AVObject> userInfo = new AVQuery<>(Constans.TABLE_USER_INFO);
        userInfo.getInBackground(objId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e != null && avObject != null) {
                    String family = avObject.getString(Constans.USER_FAMILYID);
                    if (!TextUtils.isEmpty(family)) {
                        SPUtil.put(mContext, Constans.SP_FAMILY_ID, family);
                    }
                }
                handler.sendEmptyMessage(what += 1);
            }
        });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what >= 2) {
                // 如果加载时间不到2秒就等一会，超过两秒就直接跳
                long ringTime = System.currentTimeMillis() - mStartTime;
                if (ringTime < WAIT_TIME) {
                    try {
                        Thread.sleep(WAIT_TIME - ringTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                initMain();
            }
        }
    };


    private void initMain() {
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
        layout.setAnimation(translate);
    }
}
