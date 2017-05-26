package com.lyl.myallforyou.ui.deviceinfo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.DeviceInfo;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.utils.OpenLocalMapUtil;
import com.lyl.myallforyou.view.TransitionHelper;
import com.tencent.bugly.crashreport.CrashReport;

import java.text.DecimalFormat;

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

public class DeviceInfoActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.device_manufacturer)
    TextView deviceManufacturer;
    @Bind(R.id.api_level)
    TextView apiLevel;
    @Bind(R.id.system_time)
    TextView systemTime;
    @Bind(R.id.system_battery)
    TextView systemBattery;
    @Bind(R.id.ring_volume)
    TextView ringVolume;
    @Bind(R.id.bluetooth_open)
    TextView bluetoothOpen;
    @Bind(R.id.gps_status)
    TextView gpsStatus;
    @Bind(R.id.address_location_type)
    TextView addressLocationType;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.sim_type)
    TextView simType;
    @Bind(R.id.net_type)
    TextView netType;
    @Bind(R.id.wifi_status)
    TextView wifiStatus;
    @Bind(R.id.screen_status)
    TextView screenStatus;
    @Bind(R.id.screen_brightness)
    TextView screenBrightness;
    @Bind(R.id.screen_dormant_time)
    TextView screenDormantTime;
    @Bind(R.id.all_memory)
    TextView allMemory;
    @Bind(R.id.usable_memory)
    TextView usableMemory;

    private String mTargetUuid;
    private String mTargetName;
    private DeviceInfo mDeviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);

        getParameters();

        toolbar.setTitle(mTargetName);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        getDeviceInfo();

        setupWindowAnimations();
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeviceInfo == null) {
                    return;
                }
                if ("0.0".equals(mDeviceInfo.getAddress_longitude()) || "0.0".equals(mDeviceInfo.getAddress_longitude())) {
                    showT(getString(R.string.address_error));
                } else {
                    OpenLocalMapUtil.openMap(getApplicationContext(), mDeviceInfo.getAddress_latitude(), mDeviceInfo.getAddress_longitude());
                }
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


    public void getDeviceInfo() {
        AVQuery<AVObject> avQuery = new AVQuery<AVObject>(Constans.TABLE_DEVICE_INFO);
        avQuery.whereStartsWith(DEVICE_MY_ID, mTargetUuid);
        avQuery.whereContains(DEVICE_MY_ID, mTargetUuid);
        avQuery.orderByDescending("createdAt");
        avQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null && avObject != null) {
                    saveDeviceInfo(avObject);
                } else {
                    showT(getString(R.string.net_error));
                    LogUtils.d(TAG, "mTargetUuid:" + mTargetUuid + " ; Except:" + e);
                    if (e != null) {
                        CrashReport.postCatchedException(e.getCause());
                    }
                }
            }
        });
    }


    private void saveDeviceInfo(AVObject infoDB) {
        mDeviceInfo = new DeviceInfo();

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

        setView();
    }


    private void setView() {
        String model = FS(mDeviceInfo.getDevice_model());
        String mf;
        if (TextUtils.isEmpty(model)) {
            mf = FS(mDeviceInfo.getDevice_manufacturer());
        } else {
            mf = FS(mDeviceInfo.getDevice_manufacturer()) + "(" + FS(mDeviceInfo.getDevice_model()) + ")";
        }
        deviceManufacturer.setText(mf);

        apiLevel.setText(FS(mDeviceInfo.getApi_level()));

        systemTime.setText(FS(mDeviceInfo.getSystem_date()) + " " + FS(mDeviceInfo.getSystem_time()));

        systemBattery.setText(FS(mDeviceInfo.getSystem_battery()));

        ringVolume.setText(FS(mDeviceInfo.getRing_volume()));

        if ("true".equals(FS(mDeviceInfo.getBluetooth_open()))) {
            bluetoothOpen.setText(R.string.ON);
        } else {
            bluetoothOpen.setText(R.string.OFF);
        }

        if ("true".equals(FS(mDeviceInfo.getGps_status()))) {
            gpsStatus.setText(R.string.ON);
        } else {
            gpsStatus.setText(R.string.OFF);
        }

        addressLocationType.setText(FS(mDeviceInfo.getAddress_location_type()));

        address.setText(FS(mDeviceInfo.getMy_address()));

        String sim = FS(mDeviceInfo.getSim_type());
        if (TextUtils.isEmpty(sim)) {
            simType.setText(R.string.not_sim);
            netType.setText(R.string.not_sim);
        } else {
            simType.setText(sim);

            if ("true".equals(FS(mDeviceInfo.getIs_3G()))) {
                netType.setText("3G");
            } else if ("true".equals(FS(mDeviceInfo.getIs_4G()))) {
                netType.setText("4G");
            }
        }

        if ("true".equals(FS(mDeviceInfo.getWifi_status()))) {
            String wifiName = FS(mDeviceInfo.getWifi_name());
            if (TextUtils.isEmpty(wifiName)) {
                wifiStatus.setText(getString(R.string.ON));
            } else {
                wifiStatus.setText(getString(R.string.ON) + "(" + FS(mDeviceInfo.getWifi_name()) + ")");
            }
        }

        if ("true".equals(FS(mDeviceInfo.getScreen_status()))) {
            screenStatus.setText(R.string.ON);
        } else {
            screenStatus.setText(R.string.OFF);
        }

        screenBrightness.setText(FS(mDeviceInfo.getScreen_brightness()));

        screenDormantTime.setText(FS(mDeviceInfo.getScreen_dormant_time()));

        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        double allM = Double.valueOf(FS(mDeviceInfo.getUsed_memory())) + Double.valueOf(FS(mDeviceInfo.getUsable_memory()));
        String all = decimalFormat.format(Math.round(allM / 1000));
        allMemory.setText(all + " G");

        String usable = decimalFormat.format(Math.round(Double.valueOf(mDeviceInfo.getUsable_memory()) / 1000));
        usableMemory.setText(usable + " G");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_device_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_device_more:
                Intent intent = new Intent(mContext, DeviceInfoMoreActivity.class);
                intent.putExtra(ConstantIntent.USER_INFO, mTargetUuid);
                intent.putExtra(ConstantIntent.USER_NAME, mTargetName);

                final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(DeviceInfoActivity.this, true);
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(DeviceInfoActivity.this,
                        pairs);
                startActivity(intent, transitionActivityOptions.toBundle());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
