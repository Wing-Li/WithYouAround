package com.lyl.myallforyou.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.lyl.myallforyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2017/4/21.
 */

public class OpenLocalMapUtil {

    public static void openMap(Context context, String latitude, String longitude) {
        if (OpenLocalMapUtil.isGdMapInstalled(context)) {
            gaode(context, latitude, longitude);
        } else if (OpenLocalMapUtil.isBaiduMapInstalled(context)) {
            double[] doubles = OpenLocalMapUtil.gaoDeToBaidu(Double.parseDouble(longitude), Double.parseDouble(latitude));
            String lo = String.valueOf(doubles[0]);
            String lat = String.valueOf(doubles[1]);
            baidu(context, lat, lo);
        }
    }


    public static void gaode(Context context, String lat, String lo) {
        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        i.addCategory("android.intent.category.DEFAULT");
        i.setPackage("com.autonavi.minimap");
        i.setData(Uri.parse("androidamap://viewMap?sourceApplication=" + context.getString(R.string.app_name) + "&poiname=abc&lat=" + lat +
                "&lon=" + lo + "&dev=0")); //
        context.startActivity(i);
    }


    public static void baidu(Context context, String lat, String lo) {
        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        i.setPackage("com.baidu.BaiduMap");
        // location: at,lng (先纬度，后经度) ; title: 打点标题; content	打点内容; traffic	是否开启路况，目前仅查看地图和打点支持，on表示开启，off表示关闭。
        i.setData(Uri.parse("baidumap://map/marker?location=" + lat + "," + lo + "&title=" + "TA" + "&content=" + "位置" + "&traffic=off"));
        context.startActivity(i);
    }

    /**
     * 地图应用是否安装 * @return
     */
    public static boolean isGdMapInstalled(Context context) {
        return isAvilible(context, "com.autonavi.minimap");
    }


    public static boolean isBaiduMapInstalled(Context context) {
        return isAvilible(context, "com.baidu.BaiduMap");
    }


    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    private static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 百度地图定位经纬度转高德经纬度 * @param bd_lat * @param bd_lon * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }


    /**
     * 高德地图定位经纬度转百度经纬度 * @param gd_lon * @param gd_lat * @return
     */
    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }
}
