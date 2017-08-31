package com.lyl.myallforyou.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lyl on 2017/8/31.
 */

public class PlayUtils {

    /**
     * 先检查是否安装了支付宝，如果没有安装，直接跳转到微信扫一扫，保存微信支付二维码到本地。
     * 如果安装了支付宝，跳转到个人转账页面。
     * 如果跳转失败了，则跳转到支付宝的扫一扫，保存支付宝的二维码到本地
     */
    public static void play(Context context) {
        qrPlayAlipay(context, false);
//        if (!hasInstalledAlipayClient(context.getApplicationContext())) {
//            // 跳转到微信扫描页面，选择微信的二维码
//            qrPlayAlipay(context, false);
//            return;
//        }
//
//        String play = "FKX09669ZJGDKLVHTD2VB9";
//        String intentFullUrl = "intent://platformapi/startapp?saId=10000007&" +
//                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F" + play + "%3F_s" +
//                "%3Dweb-other&_t=1472443966571#Intent;" + "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
//        try {
//            Intent intent = Intent.parseUri(intentFullUrl, Intent.URI_INTENT_SCHEME);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            // 如果跳转转账页面失败，就显示扫描页面，让用户扫描二维码
//            qrPlayAlipay(context, true);
//        }
    }

    /**
     * 支付宝/微信 扫描二维码支付
     *
     * @param isAlipay 是不是支付宝
     */
    private static void qrPlayAlipay(Context context, boolean isAlipay) {
        FileOutputStream fos = null;
        String filePath = MyApp.getAppPath() + File.separator + "play.jpg";
        try {
            File file = new File(filePath);
            if (file.exists()){
                file.delete();
            }

            // 判断是不是支付宝，保存相应二维码到本地
            int resPlay = isAlipay ? R.drawable.play_alipay : R.drawable.play_weixin;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resPlay);
            fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);

            // 通知系统，我保存了一张图片，为了在选择的时候，它在第一个
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
        } catch (Exception fileE) {
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();

                    // 根据是否是支付宝，打开相应的APP的扫描页面
                    Intent action;
                    if (isAlipay) {
                        action = new Intent(Intent.ACTION_VIEW);
                        action.setData(Uri.parse("alipayqr://platformapi/startapp?appId=10000007"));
                    } else {
                        action = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                        action.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                    }
                    context.startActivity(action);
                    Toast.makeText(context.getApplicationContext(), R.string.alipay_hint, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 判断支付宝客户端是否已安装，建议调用转账前检查
     *
     * @param context Context
     * @return 支付宝客户端是否已安装
     */
    public static boolean hasInstalledAlipayClient(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("com.eg.android.AlipayGphone", 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
