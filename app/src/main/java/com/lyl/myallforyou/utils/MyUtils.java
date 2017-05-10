package com.lyl.myallforyou.utils;

import java.util.Random;

/**
 * Created by lyl on 2017/5/10.
 */

public class MyUtils {
    /**
     * 随即获取指定位数的一串纯数字字符串
     *
     * @param num 几位数
     * @return
     */
    public static String getRandomNumber(int num) {
        long divisor = (long) Math.pow(10, num);

        Random random = new Random();
        long abs = Math.abs(random.nextLong() % divisor);
        String s = String.valueOf(abs);
        for (int i = 0; i < 10 - s.length(); i++) {
            s = "0" + s;
        }
        return s;
    }
}
