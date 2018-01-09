package com.lyl.myallforyou.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.MyShared;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.data.event.MainEvent;
import com.lyl.myallforyou.ui.BaseFragment;
import com.lyl.myallforyou.utils.SPUtil;
import com.lyl.myallforyou.view.LinearLayoutManagerWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.lyl.myallforyou.constants.Constans.USER_FAMILYID;

public class MainFragment extends BaseFragment {


    private ArrayList<UserInfo> mUserInfos;
    private MainFragmentAdapter mMyAapter;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(context));
            mUserInfos = new ArrayList<UserInfo>();
            mMyAapter = new MainFragmentAdapter(mUserInfos, mContext);
            recyclerView.setAdapter(mMyAapter);
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        initData(new MainEvent());
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initData(MainEvent event) {
        mUserInfos.clear();
        mMyAapter.notifyDataSetChanged();
        // 先添加自己
        UserInfo info = new UserInfo();
        info.setObjid(objId);
        info.setUuid(uuid);
        info.setName((String) SPUtil.get(mContext, Constans.SP_MY_NAME, ""));
        info.setNameNote(getString(R.string.myself));
        info.setSign((String) SPUtil.get(mContext, Constans.SP_MY_SGIN, ""));
        mUserInfos.add(info);

        AVQuery<AVObject> serverQuery = new AVQuery<>(Constans.TABLE_USER_INFO);
        serverQuery.whereContains(USER_FAMILYID, uuid);
        serverQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null && list.size() > 0) {
                    UserInfo info;
                    for (AVObject data : list) {
                        info = new UserInfo();
                        info.setUuid(data.getString(Constans.USER_MYID));
                        info.setName(data.getString(Constans.USER_MYNAME));
                        info.setSign(data.getString(Constans.USER_MYSGIN));
                        info.setObjid(data.getObjectId());

                        // 查询本地的用户,把本地的 id 和 备注 设置给它
                        ArrayList<UserInfo> querySD = MyApp.liteOrm.query(new QueryBuilder<UserInfo>(UserInfo.class).whereEquals(Constans
                                .SD_USER_OBJID, data.getObjectId()));
                        if (querySD.size() > 0) {
                            int id = querySD.get(0).getId();
                            info.setId(id);
                            info.setNameNote(querySD.get(0).getNameNote());
                            MyApp.liteOrm.update(info);
                        } else {
                            MyApp.liteOrm.save(info);
                        }
                        mUserInfos.add(info);
                    }
                }

                // 如果桌面 uuid 是空的，就把第一个设置为它。如果没有好友，就显示自己的信息
                MyShared myShared = new MyShared(mContext);
                String widgetUuid = myShared.getWidgetUuid();
                if (TextUtils.isEmpty(widgetUuid)){
                    if (mUserInfos.size() > 1){
                        myShared.setWidgetUuid(mUserInfos.get(1).getUuid());
                        myShared.setWidgetName(mUserInfos.get(1).getName());
                    }else {
                        myShared.setWidgetUuid(mUserInfos.get(0).getUuid());
                        myShared.setWidgetName(mUserInfos.get(0).getName());
                    }
                }

                mMyAapter.setData(mUserInfos);
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
