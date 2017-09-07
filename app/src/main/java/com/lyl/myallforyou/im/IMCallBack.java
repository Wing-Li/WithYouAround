package com.lyl.myallforyou.im;

/**
 * Created by lyl on 2017/9/6.
 */

public interface IMCallBack {
    void onSuccess(int code, String msg);

    void onFail(int code, String msg);
}
