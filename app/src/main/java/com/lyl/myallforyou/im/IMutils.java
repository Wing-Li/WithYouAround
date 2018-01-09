package com.lyl.myallforyou.im;

import android.content.Context;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.im.entity.ChatInfo;
import com.lyl.myallforyou.utils.MyUtils;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by lyl on 2017/9/6.
 */

public class IMutils {

    public static final String JPUSH_APPKEY = "d751726562f368b04f09c4a5";

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
        UserInfo info = getMyInfo();
        if (info != null) {
            info.setNickname(name);
            JMessageClient.updateMyInfo(UserInfo.Field.nickname, info, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (imCallBack == null) return;

                    if (i == 0) {
                        imCallBack.onSuccess(i, s);
                    } else {
                        imCallBack.onFail(i, s);
                    }
                }
            });
        }
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

    public static UserInfo getMyInfo() {
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo == null) {
            // 如果获取不到，就再重新获取，三次都获取不到，就返回null
            for (int i = 0; i < 3; i++) {
                myInfo = JMessageClient.getMyInfo();
                if (myInfo != null) {
                    return myInfo;
                } else {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return myInfo;
    }

    public static void getUserInfo(String username, final IMGetUserInfoCallBack userInfoCallBack) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                userInfoCallBack.onSuccess(userInfo);
            }
        });
    }

    // =================================== ↑更改用户信息↑===================================

    // =================================== ↓创建会话↓===================================
    private static Conversation createSingleConversation(String username, String appkey) {
        return Conversation.createSingleConversation(username, appkey);
    }

    /**
     * 获取单个单聊会话,没有则创建
     */
    public static Conversation getSingleConversation(String username) {
        Conversation conv = JMessageClient.getSingleConversation(username, JPUSH_APPKEY);
        if (conv == null) {
            conv = createSingleConversation(username, JPUSH_APPKEY);
        }

        return conv;
    }

    /**
     * 获取单个单聊会话,没有则创建
     */
    public static ChatInfo getSingleConversationChatInfo(String username) {
        Conversation conv = JMessageClient.getSingleConversation(username, JPUSH_APPKEY);
        if (conv == null) {
            conv = createSingleConversation(username, JPUSH_APPKEY);
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
        Conversation conv = JMessageClient.getSingleConversation(username, JPUSH_APPKEY);
        if (conv != null) {
            conv.resetUnreadCount();
        }
    }

    public static List<Message> getAllMessage(String username) {
        Conversation conv = JMessageClient.getSingleConversation(username, JPUSH_APPKEY);
        return conv.getAllMessage();
    }

    public static List<Conversation> getConversationList() {
        return JMessageClient.getConversationList();
    }

    private static void seendMessage(final Context context, Message sendMessage) {
        sendMessage.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    //消息发送成功
                } else {
                    //消息发送失败
                    MyUtils.showT(context.getApplicationContext(), R.string.send_message_fail);
                }
            }
        });
        JMessageClient.sendMessage(sendMessage);
    }

    public static void sendMessageText(Context context, Conversation conv, String str) {
        Message sendMessage = conv.createSendMessage(new TextContent(str));
        seendMessage(context, sendMessage);
    }

    public static void sendMessageImage(Context context, Conversation conv, File file) {
        Message sendMessage = null;
        try {
            if (file.exists()) {
                sendMessage = conv.createSendImageMessage(file, file.getName());
                seendMessage(context, sendMessage);
            } else {
                MyUtils.showT(context.getApplicationContext(), R.string.file_not_exists);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showT(context.getApplicationContext(), R.string.send_message_fail);
        }
    }

    public static void sendMessageVoice(Context context, Conversation conv, File file, int duration) {
        Message sendMessage = null;
        try {
            if (file.exists()) {
                sendMessage = conv.createSendVoiceMessage(file, duration);
                seendMessage(context, sendMessage);
            } else {
                MyUtils.showT(context.getApplicationContext(), R.string.file_not_exists);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showT(context.getApplicationContext(), R.string.send_message_fail);
        }
    }

    public static void sendMessageFile(Context context, Conversation conv, File file) {
        Message sendMessage = null;
        try {
            if (file.exists()) {
                sendMessage = conv.createSendFileMessage(file, file.getName());
                seendMessage(context, sendMessage);
            } else {
                MyUtils.showT(context.getApplicationContext(), R.string.file_not_exists);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showT(context.getApplicationContext(), R.string.send_message_fail);
        }
    }
    // =================================== ↑创建会话↑===================================
}
