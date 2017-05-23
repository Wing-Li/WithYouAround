package com.lyl.myallforyou.network.api;

import com.lyl.myallforyou.data.NhCommentReply;
import com.lyl.myallforyou.data.NhComments;
import com.lyl.myallforyou.data.NhEassay;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by lyl on 2017/5/9.
 */

public interface NeihanApi {

    /**
     * 获取段子
     *
     * @param content_type 图片的是-103，段子的是-102
     * @param city 城市名
     * @param am_longitude
     * @param am_latitude
     * @param min_time：上次更新时间的 Unix 时间戳，秒为单位
     * @param loc_time 当前时间 Unix 时间戳，毫秒为单位
     * @param count：返回数量
     * @param screen_width：屏幕宽度，px为单位 1450
     * @param iid ：???，一个长度为10的纯数字字符串，用于标识用户唯一性
     * @param device_id：设备 id，一个长度为11的纯数字字符串
     * @param version_code：版本号去除小数点，例如612
     * @param version_name：版本号，例如6.1.2
     * @param device_type：设备型号，例如 hongmi
     * @param device_brand：设备品牌，例如 xiaomi
     * @param os_api：操作系统版本，例如20
     * @param os_version：操作系统版本号，例如7.1.0
     * @param uuid：用户 id，一个长度为15的纯数字字符串
     * @param openudid：一个长度为16的数字和小写字母混合字符串
     * @param manifest_version_code：版本号去除小数点，例如612
     * @param resolution：屏幕宽高，例如 1920*1080
     * @param dpi：手机 dpi
     * @param update_version_code：版本号去除小数点后乘10，例如6120
     * @return
     */
    @GET("neihan/stream/mix/v1/?mpic=1&webp=1&essence=1&message_cursor=-1&do00le_col_mode=0&ac=wifi&channel=360&aid=7&app_name=joke_essay&device_platform=android&ssmix=a")
    Call<NhEassay> getNhEssay(@Query("content_type") String content_type,
                              @Query("am_city") String city,
                              @Query("am_longitude") String am_longitude,
                              @Query("am_latitude") String am_latitude,
                              @Query("min_time") long min_time,
                              @Query("am_loc_time") long loc_time,
                              @Query("count") int count,
                              @Query("screen_width") long screen_width,
                              @Query("iid") String iid,
                              @Query("device_id") String device_id,
                              @Query("version_code") String version_code,
                              @Query("version_name") String version_name,
                              @Query("device_type") String device_type,
                              @Query("device_brand") String device_brand,
                              @Query("os_api") int os_api,
                              @Query("os_version") String os_version,
                              @Query("uuid") String uuid,
                              @Query("openudid") String openudid,
                              @Query("manifest_version_code") String manifest_version_code,
                              @Query("resolution") String resolution,
                              @Query("dpi") int dpi,
                              @Query("update_version_code") String update_version_code);


    /**
     * 获取评论列表
     *
     * @param group_id
     * @param item_id
     * @param count
     * @param offset
     * @return
     */
    @GET("neihan/comments/?ac=wifi&channel=360&aid=7&app_name=joke_essay&device_platform=android&ssmix=a")
    Call<NhComments> getNhComments(@Query("group_id") String group_id,
                                   @Query("item_id") String item_id,
                                   @Query("count") int count,
                                   @Query("offset") int offset,
                                   @Query("iid") String iid,
                                   @Query("device_id") String device_id,
                                   @Query("version_code") String version_code,
                                   @Query("version_name") String version_name,
                                   @Query("device_type") String device_type,
                                   @Query("device_brand") String device_brand,
                                   @Query("os_api") int os_api,
                                   @Query("os_version") String os_version,
                                   @Query("uuid") String uuid,
                                   @Query("openudid") String openudid,
                                   @Query("manifest_version_code") String manifest_version_code,
                                   @Query("resolution") String resolution,
                                   @Query("dpi") int dpi,
                                   @Query("update_version_code") String update_version_code);


    /**
     * 获取热门评论 的二级评论
     *
     * @param id
     * @param count
     * @param offset
     * @return
     */
    @GET("2/comment/v2/reply_list/?ac=wifi&channel=360&aid=7&app_name=joke_essay&device_platform=android&ssmix=a")
    Call<NhCommentReply> getNhCommentReply(@Query("id") String id,
                                           @Query("count") int count,
                                           @Query("offset") int offset,
                                           @Query("iid") String iid,
                                           @Query("device_id") String device_id,
                                           @Query("version_code") String version_code,
                                           @Query("version_name") String version_name,
                                           @Query("device_type") String device_type,
                                           @Query("device_brand") String device_brand,
                                           @Query("os_api") int os_api,
                                           @Query("os_version") String os_version,
                                           @Query("uuid") String uuid,
                                           @Query("openudid") String openudid,
                                           @Query("manifest_version_code") String manifest_version_code,
                                           @Query("resolution") String resolution,
                                           @Query("dpi") int dpi,
                                           @Query("update_version_code") String update_version_code);


    /**
     * 下载图片
     */
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String filrUrl);
}
