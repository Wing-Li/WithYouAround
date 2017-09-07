package com.lyl.myallforyou.im.messages;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.im.IMutils;
import com.lyl.myallforyou.im.models.DefaultUser;
import com.lyl.myallforyou.im.models.MyMessage;
import com.lyl.myallforyou.im.views.ChatView;
import com.lyl.myallforyou.ui.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


public class ChatActivity extends BaseActivity implements ChatView.OnKeyboardChangedListener, ChatView
        .OnSizeChangedListener, View.OnTouchListener {

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;

    private InputMethodManager mImm;
    private Window mWindow;

    private Conversation mConv;

    private UserInfo mMyUserInfo;
    private String mTargetId;
    private String mConvTitle;
    private String mTargetName;
    private String mTargetIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();

        getParmeter();

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.setTitle(mConvTitle);

        mData = new ArrayList<>();
        initMsgAdapter();

        initKeyboard();
    }

    private void getParmeter() {
        mTargetId = getIntent().getStringExtra(Constans.TARGET_ID);
        mConvTitle = getIntent().getStringExtra(Constans.CONV_TITLE);
        mConv = IMutils.getSingleConversation(mTargetId);

        mMyUserInfo = IMutils.getMyInfo();
    }

    /**
     * 是否是自己发送的消息
     */
    private MyMessage setUserInfo(MyMessage message, boolean isMySelf) {
        if (isMySelf) {
            File file = mMyUserInfo.getAvatarFile();
            if (file != null && file.exists()) {
                message.setUserInfo(new DefaultUser("1", mMyUserInfo.getNickname(), file.getAbsolutePath()));
            }
        } else {
            message.setUserInfo(new DefaultUser("0", mTargetName, mTargetIcon));
        }
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

        return message;
    }

    private void initKeyboard() {
        mChatView.setKeyboardChangedListener(this);
        mChatView.setOnSizeChangedListener(this);
        mChatView.setOnTouchListener(this);
        // 输入框下面的菜单栏事件的监听
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            /**
             * 输入框输入文字后，点击发送按钮事件
             */
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT);
                message = setUserInfo(message, true);
                mAdapter.addToStart(message, true);

                Message sendMessage = mConv.createSendMessage(new TextContent(input.toString()));
                sendMessage.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseDesc) {
                        if (responseCode == 0) {
                            //消息发送成功
                        } else {
                            //消息发送失败
                        }
                    }
                });
                JMessageClient.sendMessage(sendMessage);
                return true;
            }

            /**
             * 选中文件或者录制完视频后，点击发送按钮触发此事件
             */
            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);

                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO);
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setMediaFilePath(item.getFilePath());
                    message = setUserInfo(message, true);

                    final MyMessage fMsg = message;
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                            try {
                                mConv.createSendFileMessage(new File(fMsg.getMediaFilePath()), fMsg.getMediaFilePath());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (JMFileSizeExceedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            /**
             * 点击语音按钮触发事件，显示录音界面前触发此事件
             * @return 返回 true 表示使用默认的界面，若返回 false 应该自己实现界面
             */
            @Override
            public boolean switchToMicrophoneMode() {
                String[] perms = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE};

                return true;
            }

            /**
             * 点击图片按钮触发事件，显示图片选择界面前触发此事件
             * @return true 表示使用默认的界面
             */
            @Override
            public boolean switchToGalleryMode() {
                String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

                return true;
            }

            /**
             * 点击拍照按钮触发事件，显示拍照界面前触发此事件
             * @return true 表示使用默认的界面
             */
            @Override
            public boolean switchToCameraMode() {
                String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO};

                File rootDir = getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/photo";
                mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss", Locale.getDefault()
                ).format(new Date()));
                return true;
            }
        });

        // 录音的接口
        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // set voice file path, after recording, audio file will save here
                String path = Environment.getExternalStorageDirectory().getPath() + "/voice";
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss", Calendar
                        .getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message = setUserInfo(message, true);
                mAdapter.addToStart(message, true);
            }

            @Override
            public void onCancelRecord() {

            }
        });

        // 相机相关的接口
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);
                message.setMediaFilePath(photoPath);
                setUserInfo(message, true);
                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {
                // 请注意，点击发送视频的事件会回调给 onSendFiles，这个是在录制完视频后触发的
            }

            @Override
            public void onCancelVideoRecord() {

            }
        });

        mChatView.setOnTouchEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
//                mAdapter.getLayoutManager().scrollToPosition(0);
            }
        });
    }

    private void initMsgAdapter() {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""), "drawable",
                            getPackageName());

                    avatarImageView.setImageResource(resId);
                } else {
                    Glide.with(getApplicationContext()).load(string).placeholder(R.drawable.aurora_headicon_default)
                            .into(avatarImageView);
                }
            }

            @Override
            public void loadImage(ImageView imageView, String string) {
                Glide.with(getApplicationContext()).load(string).fitCenter().placeholder(R.drawable
                        .aurora_picture_not_found).override(400, Target.SIZE_ORIGINAL).into(imageView);
            }
        };

        // 使用默认UI
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>(uuid, holdersConfig, imageLoader);
        // 如果你想自定义布局，创建自定义ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder 必须继承 MsgListAdapter.ViewHolders .
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO || message.getType() == IMessage
                        .MessageType.SEND_VIDEO) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(ChatActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "点击消息事件", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
                Toast.makeText(getApplicationContext(), "长按消息事件", Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Toast.makeText(getApplicationContext(), "头像点击事件", Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });

        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT);
        message = setUserInfo(message, false);
        mAdapter.addToStart(message, true);
        mAdapter.addToEnd(mData);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount <= mData.size()) {
                    Log.i("MessageListActivity", "Loading next page");
                    loadNextPage();
                }
            }
        });

        mChatView.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData);
            }
        }, 1000);
    }

    @Override
    public void onKeyBoardStateChanged(int state) {
        switch (state) {
            case ChatInputView.KEYBOARD_STATE_INIT:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (mImm != null) {
                    mImm.isActive();
                }
                if (chatInputView.getMenuState() == View.INVISIBLE || (!chatInputView.getSoftInputState() &&
                        chatInputView.getMenuState() == View.GONE)) {

                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chatInputView.dismissMenuLayout();
                }
                break;
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mChatView.setMenuHeight(oldh - h);
        }
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();

                if (view.getId() == chatInputView.getInputView().getId()) {

                    if (chatInputView.getMenuState() == View.VISIBLE && !chatInputView.getSoftInputState()) {
                        chatInputView.dismissMenuAndResetSoftMode();
                        return false;
                    } else {
                        return false;
                    }
                }
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                if (chatInputView.getSoftInputState()) {
                    View v = getCurrentFocus();

                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        chatInputView.setSoftInputState(false);
                    }
                }
                break;
        }
        return false;
    }
}
