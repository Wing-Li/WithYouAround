package com.lyl.myallforyou.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.userinfo.UserBindCallBack;

/**
 * Created by lyl on 2017/7/24.
 */

public class DialogUtils {

    /**
     * 用户登录
     */
    public static void UserBind(final Context mContext, final UserBindCallBack userBindCallBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.bind_user_txt);
        builder.setView(view);
        builder.setNegativeButton(R.string.no_old_user, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userBindCallBack.getUserInfo(null);
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String edtStr = edt.getText().toString().trim();
                if (TextUtils.isEmpty(edtStr)) return;

                MyUtils.userBind(mContext, edtStr, userBindCallBack);

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 用户解除绑定
     */
    public static void UserClear(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle(R.string.hint)//
                .setMessage(R.string.exit_user)//
                .setNegativeButton(R.string.cancel, null)//
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyUtils.clearUserInfo(mContext);
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
