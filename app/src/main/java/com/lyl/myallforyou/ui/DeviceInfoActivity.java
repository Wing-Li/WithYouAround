package com.lyl.myallforyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.DeviceInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private DeviceInfo mDeviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getParameters();
    }


    private void getParameters() {
        Intent intent = getIntent();
        mTargetUuid = intent.getStringExtra(ConstantIntent.USER_INFO);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
