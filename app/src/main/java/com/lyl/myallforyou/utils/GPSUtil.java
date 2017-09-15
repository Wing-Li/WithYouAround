package com.lyl.myallforyou.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

public class GPSUtil {

    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个经纬度坐标点间的距离
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public static double LantitudeLongitudeDist(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0) radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0) radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0) radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0) radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0) radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0) radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        // 余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 *
                EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    public static String formatDist(Double dist) {
        String result = "";

        if (dist >= 1000) {
            dist = dist / 1000;
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");
            result = decimalFormat.format(dist) + " 公里";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#0");
            result = decimalFormat.format(dist) + " 米";
        }
        return result;
    }

    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || "0".equals(str) || "0.0".equals(str)) {
            return true;
        }
        return false;
    }
}
