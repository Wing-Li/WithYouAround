package com.lyl.myallforyou.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.ui.adapter.MainFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.lyl.myallforyou.constants.Constans.USER_FAMILYID;

public class MainFragment extends BaseFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private ArrayList<UserInfo> mUserInfos;
    private MainFragmentAdapter mMyAapter;


    public static MainFragment newInstance(int columnCount) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mMyAapter = new MainFragmentAdapter(mUserInfos, mListener);
            recyclerView.setAdapter(mMyAapter);
        }
        initData();

        return view;
    }


    private OnListFragmentInteractionListener mListener = new OnListFragmentInteractionListener() {
        @Override
        public void onListFragmentInteraction(UserInfo info) {
            Intent intent = new Intent(mContext, DeviceInfoActivity.class);
            intent.putExtra(ConstantIntent.USER_INFO, info.getUuid());
            startActivity(intent);
        }
    };


    private void initData() {
        mUserInfos = new ArrayList<UserInfo>();

        // 先添加自己
        UserInfo info = new UserInfo();
        info.setUuid(uuid);
        mUserInfos.add(info);

        AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
        query.whereContains(USER_FAMILYID, uuid);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null && list.size() > 0) {
                    UserInfo info;
                    for (AVObject data : list) {
                        info = new UserInfo();
                        info.setUuid(data.getString(Constans.USER_MYID));

                        mUserInfos.add(info);
                    }
                    mMyAapter.setData(mUserInfos);
                }
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(UserInfo info);
    }
}
