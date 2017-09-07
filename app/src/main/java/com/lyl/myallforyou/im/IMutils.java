package com.lyl.myallforyou.im;

import com.lyl.myallforyou.im.entity.ChatInfo;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by lyl on 2017/9/6.
 */

public class IMutils {

    // =================================== ↓注册和登陆↓===================================

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

    private static void registerJG(final String userId, final String password, final IMCallBack imCallBack) {
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

    public static void logout() {
        JMessageClient.logout();
    }
    // =================================== ↑注册和登陆↑===================================

    // =================================== ↓更改用户信息↓===================================

    /**
     * 更新用户名称
     */
    public static void updateUserName(String name, final IMCallBack imCallBack) {
        UserInfo info = JMessageClient.getMyInfo();
        info.setNickname(name);
        JMessageClient.updateMyInfo(UserInfo.Field.nickname, info, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    imCallBack.onSuccess(i, s);
                } else {
                    imCallBack.onFail(i, s);
                }
            }
        });
    }

    /**
     * 更新用户头像
     */
    public static void updateUserIcon(File avatar, final IMCallBack imCallBack) {
        JMessageClient.updateUserAvatar(avatar, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    imCallBack.onSuccess(i, s);
                } else {
                    imCallBack.onFail(i, s);
                }
            }
        });
    }

    public static UserInfo getMyInfo(){
        return JMessageClient.getMyInfo();
    }

    // =================================== ↑更改用户信息↑===================================

    // =================================== ↓创建会话↓===================================
    private static Conversation createSingleConversation(String username, String appkey) {
        return Conversation.createSingleConversation(username, appkey);
    }

    /**
     * 获取单个单聊会话,没有则创建
     */
    public static ChatInfo getSingleConversation(String username) {
        UserInfo info = JMessageClient.getMyInfo();
        Conversation conv = JMessageClient.getSingleConversation(username, info.getAppKey());
        if (conv == null) {
            conv = createSingleConversation(username, info.getAppKey());
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setId(conv.getId());
        chatInfo.setTitle(conv.getTitle());
        chatInfo.setIcon(conv.getAvatarFile());
        chatInfo.setUnReadCount(conv.getUnReadMsgCnt());
        return chatInfo;
    }

    /**
     * 重置单个会话未读消息数
     */
    public static void resetUnreadCount(String username) {
        UserInfo info = JMessageClient.getMyInfo();
        Conversation conv = JMessageClient.getSingleConversation(username, info.getAppKey());
        if (conv != null) {
            conv.resetUnreadCount();
        }
    }

    public static List<Conversation> getConversationList() {
        return JMessageClient.getConversationList();
    }
    // =================================== ↑创建会话↑===================================
}
