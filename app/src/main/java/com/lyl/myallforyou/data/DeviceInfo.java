package com.lyl.myallforyou.data;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by lyl on 2017/4/6.
 */

@Table("device_info")
public class DeviceInfo {

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

    // 版本号
    private String version_name;

    // Cpu核心数
    private String cpu_cores_num;

    // 已经使用的内存
    private String used_memory;

    // 可用内存大小
    private String usable_memory;

    // 系统中所有的应用
    private String all_apps;

    // 屏幕亮度,范围是0-255
    private String screen_brightness;

    // 屏幕休眠时间
    private String screen_dormant_time;

    // 蓝牙是否打开
    private String bluetooth_open;

    // 铃声音量:取值范围为0-7;默认为0
    private String ring_volume;

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

    // Sim卡运营商名称
    private String sim_type;

    // 它的时间
    private String system_time;

    // 电量
    private String system_battery;


    //忽略字段，将不存储到数据库
//    @Ignore
}
