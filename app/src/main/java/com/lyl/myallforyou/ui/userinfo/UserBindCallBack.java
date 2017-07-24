package com.lyl.myallforyou.ui.userinfo;

import com.lyl.myallforyou.data.UserInfo;

/**
 * Created by lyl on 2017/7/24.
 */

public interface UserBindCallBack {
    /**
     * 返回值为 null，标志用户点了 取消 按钮
     * @param info
     */
    void getUserInfo(UserInfo info);
}
