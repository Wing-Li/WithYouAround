package com.lyl.myallforyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.utils.SPUtil;

import static com.lyl.myallforyou.constants.Constans.USER_MYID;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
                                Toast.makeText(mContext, R.string.update_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, R.string.update_success, Toast.LENGTH_SHORT).show();
                                String objectId = userInfo.getObjectId();
                                SPUtil.put(mContext, Constans.SP_USER_OBJECT_ID, objectId);
                            }
                        }
                    });
                }
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
                    initMain();
                }
            }
        });
    }


    private void initMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
