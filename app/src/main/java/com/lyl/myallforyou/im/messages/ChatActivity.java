package com.lyl.myallforyou.im.messages;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.im.IMutils;
import com.lyl.myallforyou.im.models.DefaultUser;
import com.lyl.myallforyou.im.models.MyMessage;
import com.lyl.myallforyou.im.views.ChatView;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MediaContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;


public class ChatActivity extends BaseActivity implements ChatView.OnKeyboardChangedListener, ChatView
        .OnSizeChangedListener, View.OnTouchListener {

    public static final int IMAGE_PICKER = 901;
    public static final int REQUEST_CODE_SELECT = 902;

    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.clear_tv)
    TextView clearTv;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;

    private InputMethodManager mImm;
    private Window mWindow;

    private Conversation mConv;

    private UserInfo mMyUserInfo;
    private String mTargetId;
    private String mConvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        JMessageClient.registerEventReceiver(this);

        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();

        getParmeter();

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.setTitle(mConvTitle);

        initMsgAdapter();

        initKeyboard();

        initView();

        //
        setImagePicker();
    }

    /**
     * 选择头像需要剪切，聊天页面不需要。需要重新设置
     */
    private void setImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(true);
    }

    /**
     * 退出的时候，重置设置
     */
    private void reImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);
    }

    private void initView() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)//
                        .setTitle(R.string.hint)//
                        .setMessage(R.string.delete_chat_list)//
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mConv.deleteAllMessage()) {
                                    showT(R.string.delete_success);
                                    finish();
                                } else {
                                    showT(R.string.delete_fail);
                                }
                                dialog.dismiss();
                            }
                        })//
                        .setPositiveButton(R.string.cancel, null)//
                        .create().show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        JMessageClient.exitConversation();
        reImagePicker();
        super.onDestroy();
    }

    private void getParmeter() {
        mTargetId = getIntent().getStringExtra(Constans.TARGET_ID);
        mConvTitle = getIntent().getStringExtra(Constans.CONV_TITLE);
        JMessageClient.enterSingleConversation(mTargetId);
        mConv = IMutils.getSingleConversation(mTargetId);
        mConv.resetUnreadCount();// 初始化未读数

        mMyUserInfo = IMutils.getMyInfo();

        // 加载所有信息
        mData = new ArrayList<>();
        List<Message> allMessage = mConv.getAllMessage();
        Collections.reverse(allMessage);
        for (Message msg : allMessage) {
            mData.add(megTomymsg(msg));
        }
    }

    /**
     * 是否是自己发送的消息
     */
    private MyMessage setMyUserInfo(MyMessage message) {
        File file = mMyUserInfo.getAvatarFile();
        String iconPath = file != null && file.exists() ? file.getAbsolutePath() : "";
        message.setUserInfo(new DefaultUser("1", mMyUserInfo.getNickname(), iconPath));
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

        return message;
    }

    /**
     * 是否是自己发送的消息
     */
    private MyMessage setMessageTime(Message msg, MyMessage message) {
        boolean isMe = false;
        if (MessageDirect.send == msg.getDirect()) {
            isMe = true;
        } else if (MessageDirect.receive == msg.getDirect()) {
            isMe = false;
        }
        if (isMe) {
            File file = mMyUserInfo.getAvatarFile();
            String iconPath = file != null && file.exists() ? file.getAbsolutePath() : "";
            message.setUserInfo(new DefaultUser("1", mMyUserInfo.getNickname(), iconPath));
        } else {
            UserInfo fromUser = msg.getFromUser();
            File file = fromUser.getAvatarFile();
            String iconPath = file != null && file.exists() ? file.getAbsolutePath() : "";
            message.setUserInfo(new DefaultUser("0", fromUser.getNickname(), iconPath));
        }

        Date date = new Date();
        date.setTime(msg.getCreateTime());
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));

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
                message = setMyUserInfo(message);
                mAdapter.addToStart(message, true);

                IMutils.sendMessageText(getApplicationContext(), mConv, input.toString());
                return true;
            }

            /**
             * 选中文件或者录制完视频后，点击发送按钮触发此事件
             */
            @Override
            public void onSendFiles(List<FileItem> list) {
                // 图片和照相 自己处理
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
                toSelectImage();
                ChatInputView chatInputView = mChatView.getChatInputView();
                chatInputView.dismissMenuLayout();
                return false;
            }

            /**
             * 点击拍照按钮触发事件，显示拍照界面前触发此事件
             * @return true 表示使用默认的界面
             */
            @Override
            public boolean switchToCameraMode() {
                toCamera();
                ChatInputView chatInputView = mChatView.getChatInputView();
                chatInputView.dismissMenuLayout();
                return false;
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
                message = setMyUserInfo(message);
                mAdapter.addToStart(message, true);

                IMutils.sendMessageVoice(getApplicationContext(), mConv, voiceFile, duration);
            }

            @Override
            public void onCancelRecord() {

            }
        });

        mChatView.setOnTouchEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
//                mAdapter.getLayoutManager().scrollToPosition(0);
            }
        });
    }

    private void toSelectImage() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    private void toCamera() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片 或 拍照 返回的结果
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && (requestCode == IMAGE_PICKER || requestCode == REQUEST_CODE_SELECT)) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker
                        .EXTRA_RESULT_ITEMS);

                List<FileItem> list = new ArrayList<>();
                FileItem file;
                for (ImageItem imageItem : images) {
                    file = new FileItem(imageItem.path, imageItem.name, String.valueOf(imageItem.size), String
                            .valueOf(imageItem.addTime));
                    file.setType(FileItem.Type.Image);
                    list.add(file);
                }
                // 发送 选择/拍摄 的图片
                sendImage(list);
            }
        }
    }

    private void sendImage(List<FileItem> list) {
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

            message = setMyUserInfo(message);
            message.setMediaFilePath(item.getFilePath());

            final MyMessage fMsg = message;
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(fMsg.getMediaFilePath());

                    if (fMsg.getType() == IMessage.MessageType.SEND_IMAGE) {
                        IMutils.sendMessageImage(getApplicationContext(), mConv, file);
                    } else if (fMsg.getType() == IMessage.MessageType.SEND_VIDEO) {
                        IMutils.sendMessageFile(getApplicationContext(), mConv, file);
                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    mAdapter.addToStart(fMsg, true);
                }
            });
        }
    }

    static ImageLoader imageLoader = new ImageLoader() {
        @Override
        public void loadAvatarImage(ImageView avatarImageView, String string) {
            if (!TextUtils.isEmpty(string)) {
                Glide.with(avatarImageView.getContext())//
                        .load(string)//
                        .into(avatarImageView);
            } else {
                Glide.with(avatarImageView.getContext())//
                        .load(string)//
                        .placeholder(R.drawable.aurora_headicon_default)//
                        .into(avatarImageView);
            }
        }

        @Override
        public void loadImage(ImageView imageView, String string) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(string, options); // 此时返回的bitmap为null
            int height = options.outHeight * 400 / options.outWidth;

            Glide.with(imageView.getContext()).load(string)//
                    .centerCrop()//
                    .placeholder(R.drawable.gary_bg)//
                    .override(400, height)//
                    .into(imageView);
        }
    };

    private void initMsgAdapter() {
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
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO || message.getType() == IMessage
                        .MessageType.SEND_VIDEO) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(ChatActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "点击消息事件", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
                // 长按 文字 复制
                if (message.getType() == IMessage.MessageType.RECEIVE_TEXT || message.getType() == IMessage
                        .MessageType.SEND_TEXT) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("test", message.getText());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getApplicationContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                //Toast.makeText(getApplicationContext(), "头像点击事件", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // 消息状态 view click, resend or download here
            }
        });

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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.addToEnd(mData);
//            }
//        }, 1000);
    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        MyMessage message;
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();

                message = new MyMessage(textContent.getText(), IMessage.MessageType.RECEIVE_TEXT);
                message = setMessageTime(msg, message);
                updateMessage(message);
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址

                message = new MyMessage(null, IMessage.MessageType.RECEIVE_IMAGE);
                message.setMediaFilePath(imageContent.getLocalThumbnailPath());
                message = setMessageTime(msg, message);
                updateMessage(message);
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长

                message = new MyMessage(null, IMessage.MessageType.RECEIVE_VOICE);
                message.setMediaFilePath(voiceContent.getLocalPath());
                message.setDuration(voiceContent.getDuration());
                message = setMessageTime(msg, message);
                updateMessage(message);
                break;
            case video:
                //视频消息
                MediaContent mediaContent = (VoiceContent) msg.getContent();
                mediaContent.getLocalPath();//视频文件本地地址

                message = new MyMessage(null, IMessage.MessageType.RECEIVE_VIDEO);
                message.setMediaFilePath(mediaContent.getLocalPath());
                message = setMessageTime(msg, message);
                updateMessage(message);
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
        }
        mConv.resetUnreadCount();
    }

    private void updateMessage(final MyMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToStart(message, true);
            }
        });
    }

    private MyMessage megTomymsg(Message msg) {
        MyMessage message = null;
        boolean isMe = false;
        if (MessageDirect.send == msg.getDirect()) {
            isMe = true;
        } else if (MessageDirect.receive == msg.getDirect()) {
            isMe = false;
        }

        IMessage.MessageType type = null;
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                type = isMe ? IMessage.MessageType.SEND_TEXT : IMessage.MessageType.RECEIVE_TEXT;
                message = new MyMessage(textContent.getText(), type);
                message = setMessageTime(msg, message);
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址

                type = isMe ? IMessage.MessageType.SEND_IMAGE : IMessage.MessageType.RECEIVE_IMAGE;
                message = new MyMessage(null, type);
                message.setMediaFilePath(imageContent.getLocalThumbnailPath());
                message = setMessageTime(msg, message);
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长

                type = isMe ? IMessage.MessageType.SEND_VOICE : IMessage.MessageType.RECEIVE_VOICE;
                message = new MyMessage(null, type);
                message.setMediaFilePath(voiceContent.getLocalPath());
                message.setDuration(voiceContent.getDuration());
                message = setMessageTime(msg, message);
                break;
            case video:
                //视频消息
                MediaContent mediaContent = (VoiceContent) msg.getContent();
                mediaContent.getLocalPath();//视频文件本地地址

                type = isMe ? IMessage.MessageType.SEND_VIDEO : IMessage.MessageType.RECEIVE_VIDEO;
                message = new MyMessage(null, type);
                message.setMediaFilePath(mediaContent.getLocalPath());
                message = setMessageTime(msg, message);
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
        }
        return message;
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
