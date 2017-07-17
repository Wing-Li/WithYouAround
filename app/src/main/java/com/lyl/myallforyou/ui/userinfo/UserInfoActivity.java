package com.lyl.myallforyou.ui.userinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.utils.SPUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.lyl.myallforyou.constants.Constans.SP_MY_NAME;
import static com.lyl.myallforyou.constants.Constans.SP_MY_SGIN;
import static com.lyl.myallforyou.constants.Constans.USER_MYSGIN;

public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.userinfo_name)
    TextView userinfoName;
    @Bind(R.id.userinfo_sgin)
    TextView userinfoSgin;
    @Bind(R.id.userinfo_exit)
    Button userinfoExit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.userinfo_marking)
    TextView userinfoMarking;
    @Bind(R.id.userinfo_marking_layout)
    LinearLayout userinfoMarkingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.userinfo);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        initView();
    }

    private void initView() {
        if (MyUtils.isLogin(mContext)){
            userinfoExit.setText(R.string.exit_txt);
        }else {
            userinfoExit.setText(R.string.login_txt);
        }
        userinfoExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserInfo();
            }
        });


        String spName = (String) SPUtil.get(mContext, Constans.SP_MY_NAME, "");
        if (!TextUtils.isEmpty(spName)) {
            userinfoName.setText(spName);
        }
        String spSgin = (String) SPUtil.get(mContext, Constans.SP_MY_SGIN, "");
        if (!TextUtils.isEmpty(spSgin)) {
            userinfoSgin.setText(spSgin);
        }
        String spMarking = (String) SPUtil.get(mContext, Constans.SD_MY_MARKING, "");
        if (!TextUtils.isEmpty(spMarking)) {
            userinfoMarking.setText(spMarking);
        }

        userinfoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName();
            }
        });
        userinfoSgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContent();
            }
        });
        userinfoMarkingLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setMarking();
                return true;
            }
        });
    }

    private void setUserInfo() {
        if (MyUtils.isLogin(mContext)){

        }else {

        }
    }

    /**
     * 保存用户的唯一标示
     */
    private void setMarking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.user_marking);
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
                final String edtStr = edt.getText().toString().trim();
                if (TextUtils.isEmpty(edtStr)) return;

                // 先查看是否有重复的标示
                AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
                query.whereContains(Constans.USER_MARKING, edtStr);
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null && avObject == null) {

                            AVObject todo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, objId);
                            todo.put(Constans.USER_MARKING, edtStr);
                            todo.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        showT(R.string.save_success);
                                        SPUtil.put(mContext, Constans.SD_MY_MARKING, edtStr);
                                        userinfoMarking.setText(edtStr);
                                    } else {
                                        showT(R.string.save_fail);
                                    }
                                }
                            });
                        }else {
                            showT(getString(R.string.marking_reply));
                        }
                    }
                });

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 保存名字到服务端
     */
    private void setName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.user_name);
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

                // 通过 UUID 找到用户
                AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
                query.whereContains(Constans.USER_MYID, uuid);
                query.whereStartsWith(Constans.USER_MYID, uuid);
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null && avObject != null) {
                            String objectId = avObject.getObjectId();
                            if (!objectId.equals(objId)) {
                                objId = objectId;
                                SPUtil.put(mContext, Constans.SP_OBJ_ID, objectId);
                            }

                            AVObject todo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, objId);
                            todo.put(Constans.USER_MYNAME, str);
                            todo.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        showT(R.string.save_success);
                                        SPUtil.put(mContext, SP_MY_NAME, str);
                                        userinfoName.setText(str);
                                    } else {
                                        showT(R.string.save_fail);
                                    }
                                }
                            });
                        }
                    }
                });


                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 保存签名到服务器
     */
    private void setContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.user_sign);
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
                final String str = MyUtils.checkStr(mContext, edtStr, 20, true);
                if (TextUtils.isEmpty(str)) return;

                AVObject todo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, objId);
                todo.put(USER_MYSGIN, str);
                todo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showT(R.string.save_success);
                            SPUtil.put(mContext, SP_MY_SGIN, str);
                            userinfoSgin.setText(str);
                        } else {
                            showT(R.string.save_fail);
                        }
                    }
                });

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
