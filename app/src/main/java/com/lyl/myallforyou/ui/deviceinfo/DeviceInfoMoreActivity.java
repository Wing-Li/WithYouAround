package com.lyl.myallforyou.ui.deviceinfo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.Gravity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.DeviceInfo;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.view.LinearLayoutManagerWrapper;
import com.lyl.myallforyou.view.listener.OnRecycleViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.lyl.myallforyou.constants.Constans.DEVICE_ADDRESS_LOCATION_TYPE;
import static com.lyl.myallforyou.constants.Constans.DEVICE_API_LEVEL;
import static com.lyl.myallforyou.constants.Constans.DEVICE_BLUETOOTH_OPEN;
import static com.lyl.myallforyou.constants.Constans.DEVICE_DEVICE_MANUFACTURER;
import static com.lyl.myallforyou.constants.Constans.DEVICE_DEVICE_MODEL;
import static com.lyl.myallforyou.constants.Constans.DEVICE_GPS_STATUS;
import static com.lyl.myallforyou.constants.Constans.DEVICE_IS_3G;
import static com.lyl.myallforyou.constants.Constans.DEVICE_IS_4G;
import static com.lyl.myallforyou.constants.Constans.DEVICE_MY_ADDRESS;
import static com.lyl.myallforyou.constants.Constans.DEVICE_MY_ADDRESS_LATITUDE;
import static com.lyl.myallforyou.constants.Constans.DEVICE_MY_ADDRESS_LONGITUDE;
import static com.lyl.myallforyou.constants.Constans.DEVICE_MY_ID;
import static com.lyl.myallforyou.constants.Constans.DEVICE_RING_VOLUME;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SCREEN_BRIGHTNESS;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SCREEN_DORMANT_TIME;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SCREEN_STATUS;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SIM_TYPE;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SYSTEM_BATTERY;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SYSTEM_DATE;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SYSTEM_RUNNINGTIME;
import static com.lyl.myallforyou.constants.Constans.DEVICE_SYSTEM_TIME;
import static com.lyl.myallforyou.constants.Constans.DEVICE_USABLE_MEMORY;
import static com.lyl.myallforyou.constants.Constans.DEVICE_USED_MEMORY;
import static com.lyl.myallforyou.constants.Constans.DEVICE_WIFI_NAME;
import static com.lyl.myallforyou.constants.Constans.DEVICE_WIFI_STATUS;

public class DeviceInfoMoreActivity extends BaseActivity {

    public final static int PAGE_COUNT = 10;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.device_more_recycler)
    RecyclerView deviceMoreRecycler;
    @Bind(R.id.device_more_swipe)
    SwipeRefreshLayout deviceMoreSwipe;
    private int page = 0;

    private String mTargetUuid;
    private String mTargetName;

    private List<DeviceInfo> mDeviceInfoList;
    private DeviceInfoMoreAdapter mDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info_more);
        ButterKnife.bind(this);

        getParameters();
        toolbar.setTitle(mTargetName);
        setBackUI(toolbar);

        mDeviceInfoList = new ArrayList<>();

        initView();
        getMoreDeviceInfo();
        setupWindowAnimations();
    }

    private void initView() {
        deviceMoreRecycler.setLayoutManager(new LinearLayoutManagerWrapper(mContext));
        mDeviceAdapter = new DeviceInfoMoreAdapter(mDeviceInfoList, mContext);
        deviceMoreRecycler.setAdapter(mDeviceAdapter);

        deviceMoreRecycler.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                if (!deviceMoreSwipe.isShown()){
                    deviceMoreSwipe.setRefreshing(true);
                }
                getMoreDeviceInfo();
            }
        });

        deviceMoreSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 首次进入显示的动画
            Visibility visibility = buildEnterTransition();
            getWindow().setEnterTransition(visibility);

            // 启动新 Activity ，此页面退出的动画
            visibility = buildReturnTransition();
            getWindow().setExitTransition(visibility);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildEnterTransition() {
        Slide visibility = new Slide();
        visibility.setDuration(500);
        visibility.setSlideEdge(Gravity.RIGHT);
        // 此视图将不会受到输入过渡动画的影响
        // enterTransition.excludeTarget(R.id.square_red, true);
        return visibility;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildReturnTransition() {
        Slide visibility = new Slide();
        visibility.setSlideEdge(Gravity.LEFT);
        visibility.setDuration(500);
        return visibility;
    }

    private void getParameters() {
        Intent intent = getIntent();
        mTargetUuid = intent.getStringExtra(ConstantIntent.USER_INFO);
        mTargetName = intent.getStringExtra(ConstantIntent.USER_NAME);
    }

    private void refresh() {
        mDeviceInfoList.clear();
        mDeviceAdapter.clear();
        page = 0;
        getMoreDeviceInfo();
    }

    private void getMoreDeviceInfo() {
        AVQuery<AVObject> avQuery = new AVQuery<AVObject>(Constans.TABLE_DEVICE_INFO);
        avQuery.whereStartsWith(DEVICE_MY_ID, mTargetUuid);
        avQuery.whereContains(DEVICE_MY_ID, mTargetUuid);
        avQuery.orderByDescending("createdAt");
        avQuery.limit(PAGE_COUNT);// 最多返回 10 条结果
        avQuery.skip(page * PAGE_COUNT);// 跳过 20 条结果
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                deviceMoreSwipe.setRefreshing(false);
                if (e == null && list != null && list.size() > 0) {
                    page++;
                    for (AVObject avObject : list) {
                        saveDeviceInfo(avObject);
                    }

                    mDeviceAdapter.setData(mDeviceInfoList);
                } else {
                    showT(getString(R.string.empty_data));
                }
            }
        });
    }

    private void saveDeviceInfo(AVObject infoDB) {
        DeviceInfo mDeviceInfo = new DeviceInfo();

        mDeviceInfo.setMy_id(infoDB.getString(DEVICE_MY_ID));
        mDeviceInfo.setMy_address(infoDB.getString(DEVICE_MY_ADDRESS));
        mDeviceInfo.setAddress_location_type(infoDB.getString(DEVICE_ADDRESS_LOCATION_TYPE));
        mDeviceInfo.setAddress_longitude(infoDB.getString(DEVICE_MY_ADDRESS_LONGITUDE));
        mDeviceInfo.setAddress_latitude(infoDB.getString(DEVICE_MY_ADDRESS_LATITUDE));
        mDeviceInfo.setUsed_memory(infoDB.getString(DEVICE_USED_MEMORY));
        mDeviceInfo.setUsable_memory(infoDB.getString(DEVICE_USABLE_MEMORY));
        mDeviceInfo.setScreen_status(infoDB.getString(DEVICE_SCREEN_STATUS));
        mDeviceInfo.setScreen_brightness(infoDB.getString(DEVICE_SCREEN_BRIGHTNESS));
        mDeviceInfo.setScreen_dormant_time(infoDB.getString(DEVICE_SCREEN_DORMANT_TIME));
        mDeviceInfo.setRing_volume(infoDB.getString(DEVICE_RING_VOLUME));
        mDeviceInfo.setBluetooth_open(infoDB.getString(DEVICE_BLUETOOTH_OPEN));
        mDeviceInfo.setDevice_manufacturer(infoDB.getString(DEVICE_DEVICE_MANUFACTURER));
        mDeviceInfo.setDevice_model(infoDB.getString(DEVICE_DEVICE_MODEL));
        mDeviceInfo.setApi_level(infoDB.getString(DEVICE_API_LEVEL));
        mDeviceInfo.setGps_status(infoDB.getString(DEVICE_GPS_STATUS));
        mDeviceInfo.setWifi_status(infoDB.getString(DEVICE_WIFI_STATUS));
        mDeviceInfo.setIs_3G(infoDB.getString(DEVICE_IS_3G));
        mDeviceInfo.setIs_4G(infoDB.getString(DEVICE_IS_4G));
        mDeviceInfo.setWifi_name(infoDB.getString(DEVICE_WIFI_NAME));
        mDeviceInfo.setSim_type(infoDB.getString(DEVICE_SIM_TYPE));
        mDeviceInfo.setSystem_date(infoDB.getString(DEVICE_SYSTEM_DATE));
        mDeviceInfo.setSystem_time(infoDB.getString(DEVICE_SYSTEM_TIME));
        mDeviceInfo.setSystem_battery(infoDB.getString(DEVICE_SYSTEM_BATTERY));
        mDeviceInfo.setSystem_runningtime(infoDB.getString(DEVICE_SYSTEM_RUNNINGTIME));

        mDeviceInfoList.add(mDeviceInfo);
    }
}
