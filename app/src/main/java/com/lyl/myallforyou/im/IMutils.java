package com.lyl.myallforyou.im;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by lyl on 2017/9/6.
 */

public class IMutils {

    public final static String password = "foryou";

    public static void loginJG(final String userId, final String password, final IMCallBack imCallBack) {
        JMessageClient.login(userId, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) { // 登陆成功
                    imCallBack.onSuccess(responseCode, responseMessage);
                } else if (responseCode == 801003) {
                    // 用户不存在，立即去注册
                    registerJG(userId, password, imCallBack);
                } else {
                    imCallBack.onFail(responseCode, responseMessage);
                }
            }
        });
    }

    public static void registerJG(final String userId, final String password, final IMCallBack imCallBack) {
        JMessageClient.register(userId, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    // 注册成功,立即登陆
                    loginJG(userId, password, imCallBack);
                } else {
                    imCallBack.onFail(i, s);
                }
            }
        });
    }

    public static void logout(){
        JMessageClient.logout();
    }
}
