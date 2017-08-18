package com.lyl.myallforyou.network.imp;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.DeviceInfo;
import com.tencent.bugly.crashreport.CrashReport;

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

/**
 * Created by lyl on 2017/8/18.
 */

public class DeviceInfoImp {

    public static void getDeviceInfo(String mTargetUuid, final DeviceInfoCallback deviceInfoCallback) {
        AVQuery<AVObject> avQuery = new AVQuery<AVObject>(Constans.TABLE_DEVICE_INFO);
        avQuery.whereStartsWith(DEVICE_MY_ID, mTargetUuid);
        avQuery.whereContains(DEVICE_MY_ID, mTargetUuid);
        avQuery.orderByDescending("createdAt");
        avQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null && avObject != null) {
                    DeviceInfo deviceInfo = new DeviceInfo();

                    deviceInfo.setMy_id(avObject.getString(DEVICE_MY_ID));
                    deviceInfo.setMy_address(avObject.getString(DEVICE_MY_ADDRESS));
                    deviceInfo.setAddress_location_type(avObject.getString(DEVICE_ADDRESS_LOCATION_TYPE));
                    deviceInfo.setAddress_longitude(avObject.getString(DEVICE_MY_ADDRESS_LONGITUDE));
                    deviceInfo.setAddress_latitude(avObject.getString(DEVICE_MY_ADDRESS_LATITUDE));
                    deviceInfo.setUsed_memory(avObject.getString(DEVICE_USED_MEMORY));
                    deviceInfo.setUsable_memory(avObject.getString(DEVICE_USABLE_MEMORY));
                    deviceInfo.setScreen_status(avObject.getString(DEVICE_SCREEN_STATUS));
                    deviceInfo.setScreen_brightness(avObject.getString(DEVICE_SCREEN_BRIGHTNESS));
                    deviceInfo.setScreen_dormant_time(avObject.getString(DEVICE_SCREEN_DORMANT_TIME));
                    deviceInfo.setRing_volume(avObject.getString(DEVICE_RING_VOLUME));
                    deviceInfo.setBluetooth_open(avObject.getString(DEVICE_BLUETOOTH_OPEN));
                    deviceInfo.setDevice_manufacturer(avObject.getString(DEVICE_DEVICE_MANUFACTURER));
                    deviceInfo.setDevice_model(avObject.getString(DEVICE_DEVICE_MODEL));
                    deviceInfo.setApi_level(avObject.getString(DEVICE_API_LEVEL));
                    deviceInfo.setGps_status(avObject.getString(DEVICE_GPS_STATUS));
                    deviceInfo.setWifi_status(avObject.getString(DEVICE_WIFI_STATUS));
                    deviceInfo.setIs_3G(avObject.getString(DEVICE_IS_3G));
                    deviceInfo.setIs_4G(avObject.getString(DEVICE_IS_4G));
                    deviceInfo.setWifi_name(avObject.getString(DEVICE_WIFI_NAME));
                    deviceInfo.setSim_type(avObject.getString(DEVICE_SIM_TYPE));
                    deviceInfo.setSystem_date(avObject.getString(DEVICE_SYSTEM_DATE));
                    deviceInfo.setSystem_time(avObject.getString(DEVICE_SYSTEM_TIME));
                    deviceInfo.setSystem_battery(avObject.getString(DEVICE_SYSTEM_BATTERY));
                    deviceInfo.setSystem_runningtime(avObject.getString(DEVICE_SYSTEM_RUNNINGTIME));

                    deviceInfoCallback.onSuccess(deviceInfo);
                } else {
                    if (e != null) {
                        CrashReport.postCatchedException(e.getCause());
                    }
                    deviceInfoCallback.onFail(e);
                }
            }
        });
    }

    public interface DeviceInfoCallback {
        void onSuccess(DeviceInfo deviceInfo);

        void onFail(Exception e);
    }
}
