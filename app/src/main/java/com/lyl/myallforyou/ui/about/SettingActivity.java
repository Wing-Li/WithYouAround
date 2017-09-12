package com.lyl.myallforyou.ui.about;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.dingmouren.colorpicker.ColorPickerDialog;
import com.dingmouren.colorpicker.OnColorPickerListener;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.ui.main.MainActivity;
import com.lyl.myallforyou.utils.DialogUtils;
import com.lyl.myallforyou.utils.SPUtil;
import com.lyl.myallforyou.utils.SystemRomUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lyl on 2017/5/27.
 */
public class SettingActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.setting_push)
    TextView settingPush;
    @Bind(R.id.setting_push_layout)
    LinearLayout settingPushLayout;
    @Bind(R.id.setting_auto_start)
    TextView settingAutoStart;
    @Bind(R.id.setting_auto_start_layout)
    LinearLayout settingAutoStartLayout;
    @Bind(R.id.setting_battey)
    TextView settingBattey;
    @Bind(R.id.setting_battey_layout)
    LinearLayout settingBatteyLayout;
    @Bind(R.id.setting_spacetime)
    TextView settingSpacetime;
    @Bind(R.id.setting_spacetime_layout)
    LinearLayout settingSpacetimeLayout;
    @Bind(R.id.setting_widget_color)
    View settingWidgetColor;
    @Bind(R.id.setting_widget_color_layout)
    LinearLayout settingWidgetColorLayout;
    @Bind(R.id.setting_theme)
    View settingTheme;
    @Bind(R.id.setting_theme_layout)
    LinearLayout settingThemeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.setting_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setView();
    }

    @Override
    public void onBackPressed() {
        finish();

        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
    }

    private void setView() {
        if (notificationListenerEnable()) {
            settingPush.setText(R.string.authorized);
        } else {
            settingPush.setText(R.string.unauthorized);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isIgnoreBattery(SettingActivity.this)) {
                settingBattey.setText("已开启》");
            } else {
                settingBattey.setText(R.string.unauthorized);
            }
        }
    }


    private void initView() {
        // 使用通知权限
        settingPushLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNotificationAccessSetting(mContext);
            }
        });

        // 允许开机自启
        settingAutoStartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpStartInterface(SettingActivity.this);
            }
        });

        // 大于23，并且，没有开启才设置，否则点击没反应
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isIgnoreBattery(SettingActivity.this)) {
                settingBatteyLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ignoreBatteryOptimization(SettingActivity.this);
                    }
                });
            }
        } else {
            settingBatteyLayout.setVisibility(View.GONE);
        }

        int space_time = (MyApp.UPLOAD_SPACE_TIME - 2000) / 1000 / 60;
        settingSpacetime.setText(getString(R.string.upload_space_time_txt, space_time));
        settingSpacetimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.setUploadSpaceTime(mContext);
            }
        });


        final int color = (int) SPUtil.get(mContext, Constans.SP_WIDGET_COLOR, Color.WHITE);
        settingWidgetColor.setBackgroundColor(color);
        settingWidgetColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建颜色选择器
                new ColorPickerDialog(mContext,//
                        color,//
                        false,//
                        mOnColorPickerListener//
                ).show();
            }
        });

        int i = (int) SPUtil.get(getApplicationContext(), Constans.SP_THEME, R.color.colorPrimary);
        settingTheme.setBackgroundColor(i);
        // 选择主题
        settingThemeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorChooserDialog.Builder(SettingActivity.this, R.string.select_theme)//
                        .customColors(R.array.colors, null)//
                        .doneButton(R.string.ok)//
                        .cancelButton(R.string.cancel)//
                        .allowUserColorInput(false)//
                        .allowUserColorInputAlpha(false)//
                        .show();
            }
        });
    }

    /**
     * 取色器的监听器
     */
    private OnColorPickerListener mOnColorPickerListener = new OnColorPickerListener() {
        @Override
        public void onColorCancel(ColorPickerDialog dialog) {//取消选择的颜色
        }

        @Override
        public void onColorChange(ColorPickerDialog dialog, int color) {//实时监听颜色变化
        }

        @Override
        public void onColorConfirm(ColorPickerDialog dialog, int color) {//确定的颜色
            SPUtil.put(mContext, Constans.SP_WIDGET_COLOR, color);
            settingWidgetColor.setBackgroundColor(color);
            showT(R.string.upload_space_time_success);
        }
    };

    /**
     * 判断是否拥有通知使用权
     *
     * @return
     */
    private boolean notificationListenerEnable() {
        boolean enable = false;
        String packageName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (flat != null) {
            enable = flat.contains(packageName);
        }
        return enable;
    }

    /**
     * 跳转系统设置里的通知使用权页面
     *
     * @return
     */
    private boolean gotoNotificationAccessSetting(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            try {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings" + "" + "" + "" +
                        ".Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    /**
     * 跳转到开机自启页面
     * Compatible Mainstream Models 兼容市面主流机型
     */
    public void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi") || SystemRomUtils.isMIUI()) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart" + "" +
                        "" + ".AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram" + "" +
                        ".AutoRunActivity");
            } else if (getMobileType().equals("HUAWEI") || SystemRomUtils.isEMUI()) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize" +
                        "" + ".process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu") || SystemRomUtils.isFlyme()) { //万恶的魅族
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup" + "" + "" + ""
                        + ".StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun" + "" + "" +
                        "" + ".AutoRunListActivity");
            } else {
                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
    }

    /**
     * 是否忽略电池优化
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean isIgnoreBattery(Activity activity) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        return powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
    }

    /**
     * 忽略电池优化。 之后就会弹出一个dialog
     */
    public void ignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            try {
                if (!isIgnoreBattery(activity)) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    startActivity(intent);
                }
            } catch (Exception e) {
                showT(R.string.device_not_support);
            }

        }
    }

    /**
     * 设置主题 确定按钮
     *
     * @param dialog
     * @param selectedColor
     */
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        SPUtil.put(getApplicationContext(), Constans.SP_THEME, selectedColor);

        onBackPressed();
    }

}
