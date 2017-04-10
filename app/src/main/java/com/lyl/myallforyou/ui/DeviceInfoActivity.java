package com.lyl.myallforyou.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.DeviceInfo;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.DeviceStatusUtils;
import com.lyl.myallforyou.utils.MapLocationUtil;
import com.lyl.myallforyou.utils.NetUtil;

public class DeviceInfoActivity extends AppCompatActivity {

    private Context mContext;

    private DeviceInfo mDeviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        mContext = this;
    }


    private void getParameters() {
        Intent intent = getIntent();
        mDeviceInfo = intent.getParcelableExtra(ConstantIntent.DEVICE_INFO);
    }


    private DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();

        MapLocationUtil.getInstance(mContext, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        int locationType = amapLocation.getLocationType();//定位来源

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo
                                ());
                    }
                }
            }
        });
        MapLocationUtil.startLocation();

        // 可以使用的内存
        int deviceUsableMemory = AppUtils.getDeviceUsableMemory(mContext);

        // 得到已经使用的内存
        long makedMemory = AppUtils.getMakedMemory(mContext);

        // 屏幕是否亮着
        boolean screenStatus = DeviceStatusUtils.getScreenStatus(mContext);

        // 屏幕亮度
        int screenBrightness = DeviceStatusUtils.getScreenBrightness(mContext);

        // 屏幕休眠时间
        int screenDormantTime = DeviceStatusUtils.getScreenDormantTime(mContext);

        // 铃声音量
        int ringVolume = DeviceStatusUtils.getRingVolume(mContext);

        // 蓝牙是否打开
        try {
            boolean bluetoothOpen = DeviceStatusUtils.isBluetoothOpen();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设备厂商
        String manufacturer = DeviceStatusUtils.getManufacturer();

        // 设备型号
        String model = DeviceStatusUtils.getModel();

        // api 等级
        int buildLevel = DeviceStatusUtils.getBuildLevel();

        // GPS 是否开着
        boolean gpsEnabled = NetUtil.isGpsEnabled(mContext);

        // wifi 状态
        boolean wifi = NetUtil.isWiFiStatus(mContext);

        // 获取WIFI名称
        if (NetUtil.isWifi(mContext)){
            String wifiName = NetUtil.getWifiName(mContext);
        }

        // 3G
        boolean g3 = NetUtil.is3G(mContext);

        // 4G
        boolean g4 = NetUtil.is4G(mContext);

        // Sim卡运营商名称
        String simOperatorByMnc = NetUtil.getSimOperatorByMnc(mContext);

        // 它的时间

        // 电量
        // TODO 

        // 开机时长
        String bootTime = DeviceStatusUtils.getBootTime();

        return deviceInfo;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapLocationUtil.destroyLocation();
    }
}
