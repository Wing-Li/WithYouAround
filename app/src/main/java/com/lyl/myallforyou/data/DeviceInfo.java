package com.lyl.myallforyou.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by lyl on 2017/4/6.
 */

@Table("device_info")
public class DeviceInfo implements Parcelable {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    // 自己的昵称
    @NotNull
    private String my_name;

    // 自己的ID
    @NotNull
    private String my_id;

    // 亲密人的ID号
    @NotNull
    private String family_id;

    // 详细地址
    private String my_address;

    // 定位来源
    private String sddress_location_type;

    // 已经使用的内存
    private String used_memory;

    // 可用内存大小
    private String usable_memory;

    // 系统中所有的应用
//    private String all_apps;

    // 屏幕是否亮着
    private String screen_status;

    // 屏幕亮度,范围是0-255
    private String screen_brightness;

    // 屏幕休眠时间
    private String screen_dormant_time;

    // 铃声音量:取值范围为0-7;默认为0
    private String ring_volume;

    // 蓝牙是否打开
    private String bluetooth_open;

    // 设备厂商
    private String device_manufacturer;

    // 设备型号
    private String device_model;

    // API 等级
    private String api_level;

    // GPS
    private String gps_status;
    // wifi
    private String wifi_status;
    // 3g
    private String is_3G;
    // 4g
    private String is_4G;

    // wifi 名称
    private String wifi_name;

    // Sim卡运营商名称
    private String sim_type;

    // 它的时间
    private String system_time;

    // 电量
    private String system_battery;


    //忽略字段，将不存储到数据库
//    @Ignore


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getMy_name() {
        return my_name;
    }


    public void setMy_name(String my_name) {
        this.my_name = my_name;
    }


    public String getMy_id() {
        return my_id;
    }


    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }


    public String getFamily_id() {
        return family_id;
    }


    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }


    public String getMy_address() {
        return my_address;
    }


    public void setMy_address(String my_address) {
        this.my_address = my_address;
    }


    public String getSddress_location_type() {
        return sddress_location_type;
    }


    public void setSddress_location_type(String sddress_location_type) {
        this.sddress_location_type = sddress_location_type;
    }


    public String getUsed_memory() {
        return used_memory;
    }


    public void setUsed_memory(String used_memory) {
        this.used_memory = used_memory;
    }


    public String getUsable_memory() {
        return usable_memory;
    }


    public String getWifi_name() {
        return wifi_name;
    }


    public void setWifi_name(String wifi_name) {
        this.wifi_name = wifi_name;
    }


    public void setUsable_memory(String usable_memory) {
        this.usable_memory = usable_memory;
    }


    public String getScreen_status() {
        return screen_status;
    }


    public void setScreen_status(String screen_status) {
        this.screen_status = screen_status;
    }


    public String getScreen_brightness() {
        return screen_brightness;
    }


    public void setScreen_brightness(String screen_brightness) {
        this.screen_brightness = screen_brightness;
    }


    public String getScreen_dormant_time() {
        return screen_dormant_time;
    }


    public void setScreen_dormant_time(String screen_dormant_time) {
        this.screen_dormant_time = screen_dormant_time;
    }


    public String getBluetooth_open() {
        return bluetooth_open;
    }


    public void setBluetooth_open(String bluetooth_open) {
        this.bluetooth_open = bluetooth_open;
    }


    public String getRing_volume() {
        return ring_volume;
    }


    public void setRing_volume(String ring_volume) {
        this.ring_volume = ring_volume;
    }


    public String getDevice_manufacturer() {
        return device_manufacturer;
    }


    public void setDevice_manufacturer(String device_manufacturer) {
        this.device_manufacturer = device_manufacturer;
    }


    public String getDevice_model() {
        return device_model;
    }


    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }


    public String getApi_level() {
        return api_level;
    }


    public void setApi_level(String api_level) {
        this.api_level = api_level;
    }


    public String getGps_status() {
        return gps_status;
    }


    public void setGps_status(String gps_status) {
        this.gps_status = gps_status;
    }


    public String getWifi_status() {
        return wifi_status;
    }


    public void setWifi_status(String wifi_status) {
        this.wifi_status = wifi_status;
    }


    public String getIs_3G() {
        return is_3G;
    }


    public void setIs_3G(String is_3G) {
        this.is_3G = is_3G;
    }


    public String getIs_4G() {
        return is_4G;
    }


    public void setIs_4G(String is_4G) {
        this.is_4G = is_4G;
    }


    public String getSim_type() {
        return sim_type;
    }


    public void setSim_type(String sim_type) {
        this.sim_type = sim_type;
    }


    public String getSystem_time() {
        return system_time;
    }


    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }


    public String getSystem_battery() {
        return system_battery;
    }


    public void setSystem_battery(String system_battery) {
        this.system_battery = system_battery;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.my_name);
        dest.writeString(this.my_id);
        dest.writeString(this.family_id);
        dest.writeString(this.my_address);
        dest.writeString(this.sddress_location_type);
        dest.writeString(this.used_memory);
        dest.writeString(this.usable_memory);
        dest.writeString(this.screen_status);
        dest.writeString(this.screen_brightness);
        dest.writeString(this.screen_dormant_time);
        dest.writeString(this.bluetooth_open);
        dest.writeString(this.ring_volume);
        dest.writeString(this.device_manufacturer);
        dest.writeString(this.device_model);
        dest.writeString(this.api_level);
        dest.writeString(this.gps_status);
        dest.writeString(this.wifi_status);
        dest.writeString(this.wifi_name);
        dest.writeString(this.is_3G);
        dest.writeString(this.is_4G);
        dest.writeString(this.sim_type);
        dest.writeString(this.system_time);
        dest.writeString(this.system_battery);
    }


    public DeviceInfo() {
    }


    protected DeviceInfo(Parcel in) {
        this.id = in.readInt();
        this.my_name = in.readString();
        this.my_id = in.readString();
        this.family_id = in.readString();
        this.my_address = in.readString();
        this.sddress_location_type = in.readString();
        this.used_memory = in.readString();
        this.usable_memory = in.readString();
        this.screen_status = in.readString();
        this.screen_brightness = in.readString();
        this.screen_dormant_time = in.readString();
        this.bluetooth_open = in.readString();
        this.ring_volume = in.readString();
        this.device_manufacturer = in.readString();
        this.device_model = in.readString();
        this.api_level = in.readString();
        this.gps_status = in.readString();
        this.wifi_status = in.readString();
        this.is_3G = in.readString();
        this.is_4G = in.readString();
        this.wifi_name = in.readString();
        this.sim_type = in.readString();
        this.system_time = in.readString();
        this.system_battery = in.readString();
    }


    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }


        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}
