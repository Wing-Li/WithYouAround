package com.lyl.myallforyou.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.DeviceInfo;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.DeviceStatusUtils;
import com.lyl.myallforyou.utils.LogUtils;
import com.lyl.myallforyou.utils.MapLocationUtil;
import com.lyl.myallforyou.utils.NetUtil;

import java.text.DecimalFormat;

public class DeviceInfoService extends Service {

    private Context mContext;

    private BroadcastReceiver broadcastReceiver;

    private String mBatteyStatus;
    private String mAddress;
    private String mLocationType;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        initMap();
        initBattey();
        initData();
    }


    private void initBattey() {
        // 电量
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
                    // 电池当前的电量, 它介于0和 EXTRA_SCALE之间 : 25
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    // 电池电量的最大值 : 100
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    // 电池状态 : 2
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    String batteryStatus = "";
                    switch (status) {
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                            batteryStatus = "正在充电";
                            break;
                        case BatteryManager.BATTERY_STATUS_DISCHARGING:
                            batteryStatus = "正常用电";
                            break;
                        case BatteryManager.BATTERY_STATUS_FULL:
                            batteryStatus = "充满";
                            break;
                        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                            batteryStatus = "没有充电";
                            break;
                        case BatteryManager.BATTERY_STATUS_UNKNOWN:
                            batteryStatus = "未知状态";
                            break;
                        default:
                            break;
                    }
                    mBatteyStatus = level + "/" + scale + "(" + batteryStatus + ")";
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    private void initMap() {
        MapLocationUtil.getInstance(mContext, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        // 陕西省西安市雁塔区锦业路靠近都市之门-会议中心
                        String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        // 5
                        String locationType = "";
                        int lt = amapLocation.getLocationType();//定位来源
                        switch (lt) {
                            case 0:// 定位失败
                                locationType = "0";
                                break;
                            case 1:// GPS定位
                                locationType = "GPS定位";
                                break;
                            case 2:// 前次定位
                                locationType = "前次定位";
                                break;
                            case 4:// 缓存定位
                                locationType = "缓存定位";
                                break;
                            case 5:// Wifi定位
                                locationType = "Wifi定位";
                                break;
                            case 6:// 基站定位
                                locationType = "基站定位";
                                break;
                        }
                        mAddress = address;
                        mLocationType = locationType;
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtils.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation
                                .getErrorInfo());
                    }
                }
            }
        });
        MapLocationUtil.startLocation();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }


    private void initData() {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 3 * 60 * 1000 + 2000);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final DeviceInfo deviceInfo = getDeviceInfo();
            final AVObject deviceInfoDB = new AVObject(Constans.TABLE_DEVICE_INFO);
            deviceInfoDB.put("my_id", deviceInfo.getMy_id());
            deviceInfoDB.put("my_address", deviceInfo.getMy_address());
            deviceInfoDB.put("sddress_location_type", deviceInfo.getAddress_location_type());
            deviceInfoDB.put("used_memory", deviceInfo.getUsed_memory());
            deviceInfoDB.put("usable_memory", deviceInfo.getUsable_memory());
            deviceInfoDB.put("screen_status", deviceInfo.getScreen_status());
            deviceInfoDB.put("screen_brightness", deviceInfo.getScreen_brightness());
            deviceInfoDB.put("screen_dormant_time", deviceInfo.getScreen_dormant_time());
            deviceInfoDB.put("ring_volume", deviceInfo.getRing_volume());
            deviceInfoDB.put("bluetooth_open", deviceInfo.getBluetooth_open());
            deviceInfoDB.put("device_manufacturer", deviceInfo.getDevice_manufacturer());
            deviceInfoDB.put("device_model", deviceInfo.getDevice_model());
            deviceInfoDB.put("api_level", deviceInfo.getApi_level());
            deviceInfoDB.put("gps_status", deviceInfo.getGps_status());
            deviceInfoDB.put("wifi_status", deviceInfo.getWifi_status());
            deviceInfoDB.put("is_3G", deviceInfo.getIs_3G());
            deviceInfoDB.put("is_4G", deviceInfo.getIs_4G());
            deviceInfoDB.put("wifi_name", deviceInfo.getWifi_name());
            deviceInfoDB.put("sim_type", deviceInfo.getSim_type());
            deviceInfoDB.put("system_date", deviceInfo.getSystem_date());
            deviceInfoDB.put("system_time", deviceInfo.getSystem_time());
            deviceInfoDB.put("system_battery", deviceInfo.getSystem_battery());
            deviceInfoDB.put("system_runningtime", deviceInfo.getSystem_runningtime());
            deviceInfoDB.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        deviceInfo.setIsUpload(1);
                        deviceInfo.setObject_id(deviceInfoDB.getObjectId());

                        MyApp.liteOrm.save(deviceInfo);
                    } else {

                    }
                }
            });
        }
    };


    private DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();

        String uuid = AppUtils.getUUID(mContext);
        deviceInfo.setMy_id(uuid);

        // 地理位置
        deviceInfo.setMy_address(mAddress);
        deviceInfo.setAddress_location_type(mLocationType);

        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        // 可以使用的内存 : 1190
        int deviceUsableMemory = AppUtils.getDeviceUsableMemory(mContext);
        deviceInfo.setUsable_memory(decimalFormat.format(deviceUsableMemory));

        // 得到已经使用的内存 : 2792
        int makedMemory = Math.round(AppUtils.getMakedMemory(mContext) / 1024 / 1024);
        deviceInfo.setUsed_memory(decimalFormat.format(makedMemory));

        // 屏幕是否亮着 : true
        boolean screenStatus = DeviceStatusUtils.getScreenStatus(mContext);
        deviceInfo.setScreen_status(String.valueOf(screenStatus));

        // 屏幕亮度 范围是0-255 : 152
        int screenBrightness = DeviceStatusUtils.getScreenBrightness(mContext);
        long screenLevel = Math.round((((double) screenBrightness / 255) * 10));
        deviceInfo.setScreen_brightness(screenLevel + "/10");

        // 屏幕休眠时间 : 1800000
        int screenDormantTime = DeviceStatusUtils.getScreenDormantTime(mContext);
        deviceInfo.setScreen_dormant_time(screenDormantTime / 1000 / 60 + "分钟");

        // 铃声音量 : 7
        int ringVolume = DeviceStatusUtils.getRingVolume(mContext);
        deviceInfo.setRing_volume(ringVolume + "/7");

        // 蓝牙是否打开 :false
        try {
            boolean bluetoothOpen = DeviceStatusUtils.isBluetoothOpen();
            deviceInfo.setBluetooth_open(String.valueOf(bluetoothOpen));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设备厂商 : Sony
        String manufacturer = DeviceStatusUtils.getManufacturer();
        deviceInfo.setDevice_manufacturer(manufacturer);

        // 设备型号 : E6653
        String model = DeviceStatusUtils.getModel();
        deviceInfo.setDevice_model(model);

        // api 等级 : 22
        int buildLevel = DeviceStatusUtils.getBuildLevel();
        deviceInfo.setApi_level(String.valueOf(buildLevel));

        // GPS 是否开着 : true
        boolean gpsEnabled = NetUtil.isGpsEnabled(mContext);
        deviceInfo.setGps_status(String.valueOf(gpsEnabled));

        // wifi 状态 : true
        boolean wifi = NetUtil.isWiFiStatus(mContext);
        deviceInfo.setWifi_status(String.valueOf(wifi));

        // 获取WIFI名称 : "startiasoft-mobile"
        if (NetUtil.isWifi(mContext)) {
            String wifiName = NetUtil.getWifiName(mContext);
            deviceInfo.setWifi_name(wifiName);
        }

        // 3G : false
        boolean g3 = NetUtil.is3G(mContext);
        deviceInfo.setIs_3G(String.valueOf(g3));

        // 4G : false
        boolean g4 = NetUtil.is4G(mContext);
        deviceInfo.setIs_4G(String.valueOf(g4));

        // Sim卡运营商名称 : ""
        String simOperatorByMnc = NetUtil.getSimOperatorByMnc(mContext);
        deviceInfo.setSim_type(simOperatorByMnc);

        // 它的时间 : 2017:3:11  9:24:47
        String[] systemTime = AppUtils.getSystemTime();
        String date = systemTime[0];
        String time = systemTime[1];
        deviceInfo.setSystem_date(date);
        deviceInfo.setSystem_time(time);

        // 电量
        deviceInfo.setSystem_battery(mBatteyStatus);

        // 开机时长 : 22小时36分钟
        String bootTime = DeviceStatusUtils.getBootTime();
        deviceInfo.setSystem_runningtime(bootTime);

        return deviceInfo;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MapLocationUtil.destroyLocation();
        unregisterReceiver(broadcastReceiver);
    }
}
