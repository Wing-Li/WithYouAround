package com.lyl.myallforyou.ui.essay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.NhEssay;
import com.lyl.myallforyou.network.Network;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.DeviceStatusUtils;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.utils.SPUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NhEassayActivity extends BaseActivity {

    public static final String CONTENT_TYPE = "content_type";
    public static final String CONTENT_TYPE_ESSAY = "-102";
    public static final String CONTENT_TYPE_IMAGE = "-103";

    private String version_code = "612";
    private String version_name = "6.1.2";
    private String manifest_version_code = "612";
    private String update_version_code = "6120";

    public static final int CONTENT_NUM = 10;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private NhEassayAdapter mAdapter;

    private ArrayList<NhEssay.DataBeanX.DataBean> mDataBeen;
    private String mContentType;
    private String mCity;
    private String mLatitude;
    private String mLongitude;
    private long mMinTime;
    private int mScreenWidth;
    private String mIid;
    private String mDeviceId;
    private String mUuidEassay;
    private String mDeviceType;
    private String mDeviceBrand;
    private int mOsApi;
    private String mOsVersion;
    private String mOpenudId;
    private String mResolution;
    private int mDpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nh_eassay);
        ButterKnife.bind(this);

        getParameter();
        initView();
        initData();
        getData();
    }

    private void initData() {
        mDataBeen = new ArrayList<>();

        mCity = (String) SPUtil.get(mContext, Constans.SP_CITY, "西安");
        mLatitude = (String) SPUtil.get(mContext, Constans.SP_LATITUDE, "0");
        mLongitude = (String) SPUtil.get(mContext, Constans.SP_LONGITUDE, "0");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;

        if (TextUtils.isEmpty(mIid = (String) SPUtil.get(mContext, Constans.SP_IID, ""))) {
            mIid = MyUtils.getRandomNumber(10);
            SPUtil.put(mContext, Constans.SP_IID, mIid);
        }


        if (TextUtils.isEmpty(mDeviceId = (String) SPUtil.get(mContext, Constans.SP_DEVICE_ID, ""))) {
            mDeviceId = MyUtils.getRandomNumber(11);
            SPUtil.put(mContext, Constans.SP_DEVICE_ID, mDeviceId);
        }

        mDeviceType = DeviceStatusUtils.getModel();
        mDeviceBrand = DeviceStatusUtils.getManufacturer();
        mOsApi = AppUtils.getSDKVersion();
        mOsVersion = AppUtils.getSDKVersionName();

        if (TextUtils.isEmpty(mUuidEassay = (String) SPUtil.get(mContext, Constans.SP_UUID_EASSAY, ""))) {
            mUuidEassay = MyUtils.getRandomNumber(15);
            SPUtil.put(mContext, Constans.SP_UUID_EASSAY, mUuidEassay);
        }

        mOpenudId = AppUtils.getUUID().substring(0, 16);

        mResolution = displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
        mDpi = displayMetrics.densityDpi;
    }

    private void getData() {
        Call<NhEssay> essayCall = Network.getNeihanApi().getNhEssay(mContentType,// 图片的是-103，段子的是-102
                mCity,// 城市
                mLongitude,//
                mLatitude,//
                mMinTime,// 上次更新时间的 Unix 时间戳，秒为单位
                mMinTime = System.currentTimeMillis(),// 当前时间 Unix 时间戳，毫秒为单位
                CONTENT_NUM,// 返回数量
                mScreenWidth,// 屏幕宽度，px为单位 1450
                mIid,// 一个长度为10的纯数字字符串，用于标识用户唯一性
                mDeviceId,// 设备 id，一个长度为11的纯数字字符串
                version_code,// 版本号去除小数点，例如612
                version_name,// 版本号，例如6.1.2
                mDeviceType,// 设备型号，例如 hongmi
                mDeviceBrand,// 设备品牌，例如 xiaomi
                mOsApi, // 操作系统版本，例如20
                mOsVersion,// 操作系统版本号，例如7.1.0
                mUuidEassay,// 用户 id，一个长度为15的纯数字字符串
                mOpenudId,// 一个长度为16的数字和小写字母混合字符串
                manifest_version_code,// 版本号去除小数点，例如612
                mResolution,// 屏幕宽高，例如 1920*1080
                mDpi,// 手机 dpi
                update_version_code);
        Call<NhEssay> clone = essayCall.clone();
        clone.enqueue(new Callback<NhEssay>() {
            @Override
            public void onResponse(Call<NhEssay> call, Response<NhEssay> response) {
                Thread thread = Thread.currentThread();
                String name = thread.getName();
                if (response.isSuccessful()) {
                    NhEssay body = response.body();
                    LogUtils.d(body.toString());
                } else {
                    LogUtils.e(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NhEssay> call, Throwable t) {
                Thread thread = Thread.currentThread();
                String name = thread.getName();
                Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
                LogUtils.e(t.getMessage());
            }
        });
    }

    private void initView() {
        toolbar.setTitle(R.string.nheassay);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        mAdapter = new NhEassayAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                getData();
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataBeen.clear();
                getData();
            }
        });
    }

    public void getParameter() {
        Intent intent = getIntent();
        mContentType = intent.getStringExtra(CONTENT_TYPE);
    }
}
