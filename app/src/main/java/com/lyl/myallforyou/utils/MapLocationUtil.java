package com.lyl.myallforyou.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lyl.myallforyou.MyApp;

/**
 * Created by lyl on 2017/4/6.
 */

public class MapLocationUtil {

    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public static AMapLocationClientOption mLocationOption = null;

    private static MapLocationUtil mapLocationUtil = new MapLocationUtil();


    private MapLocationUtil() {
    }


    public static MapLocationUtil getInstance(Context context, AMapLocationListener mLocationListener) {
        mapLocationUtil.initLoaction(context, mLocationListener);
        return mapLocationUtil;
    }


    public void initLoaction(Context context, AMapLocationListener mLocationListener) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 获取最近3s内精度最高的一次定位结果：
        // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        // mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setInterval(MyApp.MAP_SPACE_TIME);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新，相应的会多付出一些电量消耗。
        mLocationOption.setWifiActiveScan(false);
    }


    /**
     * 启动定位
     */
    public static void startLocation() {
        if (mLocationClient != null && mLocationOption != null) {
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }


    /**
     * 停止定位后，本地定位服务并不会被销毁
     */
    public static void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }


    /**
     * 销毁定位客户端，同时销毁本地定位服务。
     */
    public static void destroyLocation() {
        if (mLocationClient != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stopLocation();
            }
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }
}
