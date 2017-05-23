package com.lyl.myallforyou.network.imp;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.NhCommentReply;
import com.lyl.myallforyou.data.NhComments;
import com.lyl.myallforyou.data.NhEassay;
import com.lyl.myallforyou.network.Network;
import com.lyl.myallforyou.utils.AppUtils;
import com.lyl.myallforyou.utils.DeviceStatusUtils;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.utils.SPUtil;

import retrofit2.Call;

/**
 * Created by lyl on 2017/5/23.
 */

public class NeihanImp {

    private String version_code = "612";
    private String version_name = "6.1.2";
    private String manifest_version_code = "612";
    private String update_version_code = "6120";

    public static final int CONTENT_NUM = 15;

    private Activity mContext;

    private String mCity;
    private String mLatitude;
    private String mLongitude;
    private long mMinTime;
    private int mScreenWidth;
    private String mIid;
    private String mDeviceId;
    private String mUuidEassay;
    private String mDeviceType;
    private String mDeviceBrand;
    private int mOsApi;
    private String mOsVersion;
    private String mOpenudId;
    private String mResolution;
    private int mDpi;

    public NeihanImp(Activity context) {
        mContext = context;
        initData();
    }

    private void initData() {

        mCity = (String) SPUtil.get(mContext, Constans.SP_CITY, "西安");
        mLatitude = (String) SPUtil.get(mContext, Constans.SP_LATITUDE, "0");
        mLongitude = (String) SPUtil.get(mContext, Constans.SP_LONGITUDE, "0");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;

        if (TextUtils.isEmpty(mIid = (String) SPUtil.get(mContext, Constans.SP_IID, ""))) {
            mIid = MyUtils.getRandomNumber(10);
            SPUtil.put(mContext, Constans.SP_IID, mIid);
        }

        if (TextUtils.isEmpty(mDeviceId = (String) SPUtil.get(mContext, Constans.SP_DEVICE_ID, ""))) {
            mDeviceId = MyUtils.getRandomNumber(11);
            SPUtil.put(mContext, Constans.SP_DEVICE_ID, mDeviceId);
        }

        mDeviceType = DeviceStatusUtils.getModel();
        mDeviceBrand = DeviceStatusUtils.getManufacturer();
        mOsApi = AppUtils.getSDKVersion();
        mOsVersion = AppUtils.getSDKVersionName();

        if (TextUtils.isEmpty(mUuidEassay = (String) SPUtil.get(mContext, Constans.SP_UUID_EASSAY, ""))) {
            mUuidEassay = MyUtils.getRandomNumber(15);
            SPUtil.put(mContext, Constans.SP_UUID_EASSAY, mUuidEassay);
        }

        mOpenudId = AppUtils.getUUID().substring(0, 16);

        mResolution = displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
        mDpi = displayMetrics.densityDpi;
    }

    public Call<NhEassay> getNhEssayDetails(String mContentType) {
        return Network.getNeihanApi().getNhEssay(mContentType,// 图片的是-103，段子的是-102
                mCity,// 城市
                mLongitude,//
                mLatitude,//
                mMinTime,// 上次更新时间的 Unix 时间戳，秒为单位
                mMinTime = System.currentTimeMillis(),// 当前时间 Unix 时间戳，毫秒为单位
                CONTENT_NUM,// 返回数量
                mScreenWidth,// 屏幕宽度，px为单位 1450
                mIid,// 一个长度为10的纯数字字符串，用于标识用户唯一性
                mDeviceId,// 设备 id，一个长度为11的纯数字字符串
                version_code,// 版本号去除小数点，例如612
                version_name,// 版本号，例如6.1.2
                mDeviceType,// 设备型号，例如 hongmi
                mDeviceBrand,// 设备品牌，例如 xiaomi
                mOsApi, // 操作系统版本，例如20
                mOsVersion,// 操作系统版本号，例如7.1.0
                mUuidEassay,// 用户 id，一个长度为15的纯数字字符串
                mOpenudId,// 一个长度为16的数字和小写字母混合字符串
                manifest_version_code,// 版本号去除小数点，例如612
                mResolution,// 屏幕宽高，例如 1920*1080
                mDpi,// 手机 dpi
                update_version_code);
    }

    public Call<NhComments> getNhComments(String group_id, int page) {
        return Network.getNeihanApi().getNhComments(group_id,// 段子group_id
                group_id,//
                CONTENT_NUM,// 数量
                page * CONTENT_NUM,// 跳过 n 条
                mIid,// 一个长度为10的纯数字字符串，用于标识用户唯一性
                mDeviceId,// 设备 id，一个长度为11的纯数字字符串
                version_code,// 版本号去除小数点，例如612
                version_name,// 版本号，例如6.1.2
                mDeviceType,// 设备型号，例如 hongmi
                mDeviceBrand,// 设备品牌，例如 xiaomi
                mOsApi, // 操作系统版本，例如20
                mOsVersion,// 操作系统版本号，例如7.1.0
                mUuidEassay,// 用户 id，一个长度为15的纯数字字符串
                mOpenudId,// 一个长度为16的数字和小写字母混合字符串
                manifest_version_code,// 版本号去除小数点，例如612
                mResolution,// 屏幕宽高，例如 1920*1080
                mDpi,// 手机 dpi
                update_version_code);
    }

    public Call<NhCommentReply> getNhCommentReply(String id, int page) {
        return Network.getNeihanApi().getNhCommentReply(id,// 段子group_id
                CONTENT_NUM,// 数量
                page * CONTENT_NUM,// 跳过 n 条
                mIid,// 一个长度为10的纯数字字符串，用于标识用户唯一性
                mDeviceId,// 设备 id，一个长度为11的纯数字字符串
                version_code,// 版本号去除小数点，例如612
                version_name,// 版本号，例如6.1.2
                mDeviceType,// 设备型号，例如 hongmi
                mDeviceBrand,// 设备品牌，例如 xiaomi
                mOsApi, // 操作系统版本，例如20
                mOsVersion,// 操作系统版本号，例如7.1.0
                mUuidEassay,// 用户 id，一个长度为15的纯数字字符串
                mOpenudId,// 一个长度为16的数字和小写字母混合字符串
                manifest_version_code,// 版本号去除小数点，例如612
                mResolution,// 屏幕宽高，例如 1920*1080
                mDpi,// 手机 dpi
                update_version_code);
    }

}
