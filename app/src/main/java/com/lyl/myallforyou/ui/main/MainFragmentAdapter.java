package com.lyl.myallforyou.ui.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.ui.deviceinfo.DeviceInfoActivity;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.view.TransitionHelper;

import java.util.List;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {

    private Activity mContext;
    private List<UserInfo> mValues;


    public MainFragmentAdapter(List<UserInfo> items, Activity context) {
        mValues = items;
        mContext = context;
    }


    public void setData(List<UserInfo> data) {
        mValues.clear();
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

        // TODO 加载图片
        holder.icon.setImageResource(R.mipmap.icon);

        String name = userInfo.getName();
        String nameNote = userInfo.getNameNote();
        if (TextUtils.isEmpty(name)) {
            name = userInfo.getNameNote();
        } else {
            if (!TextUtils.isEmpty(nameNote)) {
                name = name + " (" + userInfo.getNameNote() + ")";
            }
        }
        holder.name.setText(name);

        holder.content.setText(userInfo.getSign());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipDeviceInfo(userInfo);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (position != 0) {
                    changeNote(userInfo, position);
                }
                return true;
            }
        });
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


        public ViewHolder(View view) {
            super(view);
            mView = view;
            icon = (ImageView) view.findViewById(R.id.item_icon);
            name = (TextView) view.findViewById(R.id.item_name);
            content = (TextView) view.findViewById(R.id.item_content);
        }
    }


    /**
     * 跳转详情页面
     */
    private void skipDeviceInfo(UserInfo userInfo) {
        Intent intent = new Intent(mContext, DeviceInfoActivity.class);
        intent.putExtra(ConstantIntent.USER_INFO, userInfo.getUuid());

        String name = userInfo.getName();
        String nameNote = userInfo.getNameNote();
        if (TextUtils.isEmpty(name)) {
            name = userInfo.getNameNote();
        } else {
            if (!TextUtils.isEmpty(nameNote)) {
                name = name + " (" + userInfo.getNameNote() + ")";
            }
        }
        intent.putExtra(ConstantIntent.USER_NAME, name);

        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(mContext, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, pairs);
        mContext.startActivity(intent, transitionActivityOptions.toBundle());
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
