package com.lyl.myallforyou.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.SPUtil;

/**
 * Created by lyl on 2017/4/18.
 */

public class BaseFragment extends Fragment {

    public Activity mContext;

    protected String uuid;
    protected String objId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        initData();
    }


    private void initData() {
        uuid = (String) SPUtil.get(mContext, Constans.SP_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = AppUtils.getUUID(mContext);
            SPUtil.put(mContext, Constans.SP_UUID, uuid);
        }

        objId = (String) SPUtil.get(mContext, Constans.SP_USER_OBJECT_ID, "");
    }
}
