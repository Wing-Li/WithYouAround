package com.lyl.myallforyou.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.lyl.myallforyou.BuildConfig;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.ui.main.MainActivity;
import com.lyl.myallforyou.ui.userinfo.UserBindCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lyl on 2017/5/10.
 */

public class MyUtils {

    public static boolean isDev() {
        if ("dev".equals(BuildConfig.Environment)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用户是否登录
     */
    public static boolean isLogin(Context mContext) {
        String spMarking = (String) SPUtil.get(mContext, Constans.SD_MY_MARKING, "");
        return !TextUtils.isEmpty(spMarking);
    }

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
     * 根据时间戳 返回时间 12-14 13:25
     *
     * @param cur
     * @return
     */
    public static String getDate(long cur) {
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(cur);
//        return c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar
// .MINUTE);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(cur));
    }

    /**
     * 用户 退出
     */
    public static void clearUserInfo(Context context) {
        SPUtil.put(context,Constans.SP_OBJ_ID,"");
        SPUtil.put(context,Constans.SP_UUID,"");
        SPUtil.put(context,Constans.SP_MY_NAME,"");
        SPUtil.put(context,Constans.SP_MY_SGIN,"");
        SPUtil.put(context,Constans.SD_MY_MARKING,"");

        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(MainActivity.TAG_EXIT, true);
        context.startActivity(intent);
    }

    /**
     * 用户登录
     * @param context
     * @param tag
     * @param userBindCallBack
     */
    public static void userBind(final Context context, final String tag, final UserBindCallBack userBindCallBack) {
        // 先查看是否有重复的标示
        AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
        query.whereContains(Constans.USER_MARKING, tag);
        query.whereStartsWith(Constans.USER_MARKING, tag);
        query.whereEndsWith(Constans.USER_MARKING, tag);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject data, AVException e) {
                if (e == null && data != null) {
                    UserInfo info = new UserInfo();
                    info.setObjid(data.getObjectId());
                    info.setUuid(data.getString(Constans.USER_MYID));
                    info.setName(data.getString(Constans.USER_MYNAME));
                    info.setSign(data.getString(Constans.USER_MYSGIN));
                    info.setMarking(tag);

                    SPUtil.put(context, Constans.SP_OBJ_ID, info.getObjid());
                    SPUtil.put(context, Constans.SP_UUID, info.getUuid());
                    SPUtil.put(context, Constans.SP_MY_NAME, info.getName());
                    SPUtil.put(context, Constans.SP_MY_SGIN, info.getSign());
                    SPUtil.put(context, Constans.SD_MY_MARKING, info.getMarking());

                    userBindCallBack.getUserInfo(info);
                } else {
                    Toast.makeText(context, R.string.marking_not, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
