package com.lyl.myallforyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.DeviceInfo;

public class DeviceInfoActivity extends AppCompatActivity {

    private DeviceInfo mDeviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

    }


    private void getParameters() {
        Intent intent = getIntent();
        mDeviceInfo = intent.getParcelableExtra(ConstantIntent.DEVICE_INFO);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
