package com.lyl.myallforyou.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.lyl.myallforyou.R;

import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 检查输入的字符串
     *
     * @param str           被检查的内容
     * @param length        要求的长度
     * @param isSpecialChar 是否可以输入特殊字符
     * @return 符合要求则返回原字符串；否则，返回 空字符串
     */
    public static String checkStr(Context mContext, String str, int length, boolean isSpecialChar) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
            return "";
        }
        if (str.length() > length) {
            Toast.makeText(mContext, mContext.getString(R.string.length_error, length), Toast.LENGTH_SHORT).show();
            return "";
        }
        if (!isSpecialChar) {
            String zhengze = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
            Pattern pattern = Pattern.compile(zhengze);
            Matcher matcher = pattern.matcher(str);
            if (!matcher.matches()) {
                Toast.makeText(mContext, R.string.style_error, Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        return str;
    }

    /**
     *  根据时间戳 返回时间 12-14 13:25
     * @param cur
     * @return
     */
    public static String getDate(long cur) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(cur);
        return c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    }
}
