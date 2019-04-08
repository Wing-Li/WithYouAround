package com.lyl.myallforyou.ui.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.MyShared;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.data.event.MainEvent;
import com.lyl.myallforyou.im.IMutils;
import com.lyl.myallforyou.im.messages.ChatActivity;
import com.lyl.myallforyou.ui.deviceinfo.DeviceInfoActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.utils.SPUtil;
import com.lyl.myallforyou.view.TransitionHelper;
import com.lyl.myallforyou.widget.WidgetService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.im.android.api.model.Conversation;

import static com.lyl.myallforyou.constants.Constans.USER_FAMILYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYID;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {

    private Activity mContext;
    private List<UserInfo> mValues;
    private MyShared myShared;


    public MainFragmentAdapter(List<UserInfo> items, Activity context) {
        mValues = items;
        mContext = context;
        myShared = new MyShared(context);
    }


    public void setData(List<UserInfo> data) {
        mValues = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_main, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserInfo userInfo = mValues.get(position);
        int readCount = 0;
        if (position == 0) { // 第一条是自己，直接显示头像
            String iconPath = (String) SPUtil.get(mContext, Constans.SP_MY_ICON, "");
            if (TextUtils.isEmpty(iconPath)) {
                holder.icon.setImageResource(R.mipmap.icon);
            } else {
                ImgUtils.loadCircle(mContext, iconPath, holder.icon);
            }
        } else { // 之后每次请求头像 和 未读数
            final Conversation conv = IMutils.getSingleConversation(userInfo.getUuid());
            if (conv != null) {
                cn.jpush.im.android.api.model.UserInfo info = (cn.jpush.im.android.api.model.UserInfo) conv
                        .getTargetInfo();
                File icon = info.getAvatarFile();
                if (icon != null && icon.exists()) {
                    ImgUtils.loadCircle(mContext, Uri.fromFile(icon), holder.icon);
                } else {
                    holder.icon.setImageResource(R.mipmap.icon);
                }

                readCount = conv.getUnReadMsgCnt();
            }else {
                // 没有会话，并且创建失败，使用默认头像
                holder.icon.setImageResource(R.mipmap.icon);
            }

            if (readCount > 0) {
                holder.unRead.setText(String.valueOf(readCount));
                holder.unRead.setVisibility(View.VISIBLE);
            } else {
                holder.unRead.setVisibility(View.GONE);
            }
        }

        String name = userInfo.getName();
        String nameNote = userInfo.getNameNote();
        if (TextUtils.isEmpty(name)) {
            name = userInfo.getNameNote();
        } else {
            if (!TextUtils.isEmpty(nameNote)) {
                name = name + " (" + userInfo.getNameNote() + ")";
            }
        }
        final String finalName = name;
        holder.name.setText(finalName);

        holder.content.setText(userInfo.getSign());


        final int finalReadCount = readCount;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalReadCount > 0) {
                    skipChatActivity(userInfo.getUuid(), finalName);
                } else {
                    skipDeviceInfo(userInfo);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (position != 0) {// 第一个是自己
                    selectItem(userInfo, position);
                }
                return true;
            }
        });

        if (myShared.getWidgetUuid().equals(userInfo.getUuid())) {
            holder.star.setVisibility(View.VISIBLE);
        } else {
            holder.star.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView icon;
        public final TextView name;
        public final TextView content;
        public final ImageView star;
        public final TextView unRead;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            icon = (ImageView) view.findViewById(R.id.item_icon);
            name = (TextView) view.findViewById(R.id.item_name);
            content = (TextView) view.findViewById(R.id.item_content);
            star = (ImageView) view.findViewById(R.id.item_star);
            unRead = (TextView) view.findViewById(R.id.item_unread_count);
        }
    }

    private void skipChatActivity(String convId, String convTitle) {
        cn.jpush.im.android.api.model.UserInfo myInfo = IMutils.getMyInfo();
        if (myInfo != null){
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra(Constans.TARGET_ID, convId);
            intent.putExtra(Constans.CONV_TITLE, convTitle);
            mContext.startActivity(intent);
        }else {
            MyUtils.showT(mContext, R.string.im_un_init);
        }
    }

    /**
     * 跳转详情页面
     */
    private void skipDeviceInfo(UserInfo userInfo) {
        Intent intent = new Intent(mContext, DeviceInfoActivity.class);
        intent.putExtra(ConstantIntent.USER_INFO, userInfo.getUuid());

        String name = getName(userInfo);
        intent.putExtra(ConstantIntent.USER_NAME, name);

        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(mContext, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation
                (mContext, pairs);
        mContext.startActivity(intent, transitionActivityOptions.toBundle());
    }

    private String getName(UserInfo userInfo) {
        String name = userInfo.getName();
        String nameNote = userInfo.getNameNote();
        if (TextUtils.isEmpty(name)) {
            name = userInfo.getNameNote();
        } else {
            if (!TextUtils.isEmpty(nameNote)) {
                name = name + " (" + userInfo.getNameNote() + ")";
            }
        }
        return name;
    }

    private void selectItem(final UserInfo userInfo, final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setItems(R.array.main_select_item, new
                DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 这里是按填入的 数组的顺序排的，写常量没意义
                switch (i) {
                    case 0:// 查看
                        skipDeviceInfo(userInfo);
                        break;
                    case 1:// 发送消息
                        skipChatActivity(userInfo.getUuid(), getName(userInfo));
                        break;
                    case 2://设为星标
                        setStar(userInfo);
                        break;
                    case 3:// 修改备注
                        changeNote(userInfo, position);
                        break;
                    case 4:// 删除密友
                        deleteFrined(userInfo);
                        break;
                    case 5:// 取消
                        break;
                }

            }
        }).create();
        alertDialog.show();

    }

    /**
     * 设为星标用户
     */
    private void setStar(UserInfo userInfo) {
        myShared.setWidgetUuid(userInfo.getUuid());
        String name = userInfo.getName();
        String nameNote = userInfo.getNameNote();
        if (TextUtils.isEmpty(name)) {
            name = userInfo.getNameNote();
        } else {
            if (!TextUtils.isEmpty(nameNote)) {
                name = name + " (" + userInfo.getNameNote() + ")";
            }
        }
        myShared.setWidgetName(name);

        Toast.makeText(mContext.getApplicationContext(), R.string.setting_success, Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();

        Intent updateIntent = new Intent(WidgetService.ACTION_UPDATE_ALL);
        mContext.sendBroadcast(updateIntent);
    }

    private Handler mHandler;

    /**
     * 删除密友
     */
    private void deleteFrined(final UserInfo userInfo) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)//
                .setTitle(R.string.delete)//
                .setMessage(R.string.delete_friend_dialog_msg)//
                .setPositiveButton(R.string.cancel, null)//
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uuid = (String) SPUtil.get(mContext, Constans.SP_UUID, "");
                        String objId = (String) SPUtil.get(mContext, Constans.SP_OBJ_ID, "");
                        mHandler = new Handler();
                        deleteUser(userInfo, uuid, objId);
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 绑定扫描到的数据
     *
     * @param uuid  我的uuid
     * @param objId 我的objId
     */
    private void deleteUser(final UserInfo userInfo, final String uuid, final String objId) {
        // 查询对方的数据
        AVQuery<AVObject> familyQuery = new AVQuery<>(Constans.TABLE_USER_INFO);
        familyQuery.whereContains(USER_MYID, userInfo.getUuid());
        familyQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject familyObject, AVException e) {
                if (e == null && familyObject != null) {
                    // 查询自己的数据
                    AVQuery<AVObject> myQuery = new AVQuery<>(Constans.TABLE_USER_INFO);
                    myQuery.whereContains(USER_MYID, uuid);
                    myQuery.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject myObject, AVException e) {
                            if (e == null && myObject != null) {
                                ArrayList<AVObject> todos = new ArrayList<AVObject>();

                                // 把对方从我的表里删除
                                String myFamily = myObject.getString(USER_FAMILYID);
                                AVObject my = deleteFamilyToMy(objId, userInfo.getUuid(), myFamily);
                                todos.add(my);

                                // 把我从对方的表里删除
                                final String familyObjId = familyObject.getObjectId();
                                String familyId = (String) familyObject.get(USER_FAMILYID);
                                AVObject family = deleteFamilyToMy(familyObjId, uuid, familyId);
                                todos.add(family);

                                AVObject.saveAllInBackground(todos, new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            // 删除本地存储的用户
                                            UserInfo userInfo = null;
                                            try {
                                                userInfo = new UserInfo();
                                                userInfo.setId(userInfo.getId());
                                                userInfo.setObjid(familyObjId);
                                                userInfo.setUuid(userInfo.getUuid());
                                                userInfo.setName(familyObject.getString(Constans.USER_MYNAME));
                                                userInfo.setSign(familyObject.getString(Constans.USER_MYSGIN));
                                                MyApp.liteOrm.delete(userInfo);
                                            } catch (Exception ex) {
                                            }

                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    notifyDataSetChanged();
                                                    Toast.makeText(mContext, R.string.delete_success, Toast
                                                            .LENGTH_SHORT).show();
                                                    EventBus.getDefault().post(new MainEvent());
                                                }
                                            });

                                        } else {
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, R.string.delete_fail, Toast
                                                            .LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else {
                    Toast.makeText(mContext, R.string.not_my_object_id, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 将 family 从 my 删除
     *
     * @param my         我的 objectId
     * @param family     对方 uuid
     * @param myFamilyId 与我绑定的人id
     */
    private AVObject deleteFamilyToMy(String my, String family, String myFamilyId) {
        AVObject userInfo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, my);
        if (TextUtils.isEmpty(myFamilyId) || !myFamilyId.contains(family)) {
            return userInfo;
        } else {
            String[] split = myFamilyId.split(",");
            String saveFamilyid = "";
            for (String s : split) {
                if (!s.equals(family)) {
                    if (TextUtils.isEmpty(saveFamilyid)) {
                        saveFamilyid = s;
                    } else {
                        saveFamilyid = saveFamilyid + "," + s;
                    }
                }
            }
            userInfo.put(USER_FAMILYID, saveFamilyid);
        }
        return userInfo;
    }

    /**
     * 长按设置备注
     */
    private void changeNote(final UserInfo userInfo, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.dialog_namenote_title);
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String edtStr = edt.getText().toString().trim();
                final String str = MyUtils.checkStr(mContext, edtStr, 6, false);
                if (TextUtils.isEmpty(str)) return;

                userInfo.setNameNote(str);
                MyApp.liteOrm.update(userInfo);

                mValues.get(pos).setNameNote(str);
                notifyItemChanged(pos);

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
