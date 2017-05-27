package com.lyl.myallforyou.ui.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.AppUtils;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    private static final String groupKey = "kUrVbUBIA5RYbYHqBxd69lJxXA69zK4h";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.about_version_layout)
    LinearLayout aboutVersionLayout;
    @Bind(R.id.about_version)
    TextView aboutVersion;
    @Bind(R.id.about_email)
    TextView aboutEmail;
    @Bind(R.id.about_ask_friend)
    LinearLayout aboutAskFriend;
    @Bind(R.id.about_add_group)
    TextView aboutAddGroup;
    @Bind(R.id.about_add_group_layout)
    LinearLayout aboutAddGroupLayout;
    @Bind(R.id.about_back_running)
    LinearLayout aboutBackRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.about);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        initView();
    }


    private void initView() {
        String verName = AppUtils.getVerName(mContext);
        aboutVersion.setText("v." + verName);

        aboutEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("text", aboutEmail.getText()));
                showT(getString(R.string.capy_email));
                return true;
            }
        });

        aboutVersionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackRunning();
            }
        });

        aboutAskFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });

        aboutAddGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup(groupKey);
            }
        });

        aboutBackRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private long startTime = 0;
    private int clickNum = 0;

    private void showBackRunning() {
        if (System.currentTimeMillis() - startTime < 2000) {
            if (clickNum > 5) {
                aboutBackRunning.setVisibility(View.VISIBLE);
            }
            clickNum = ++clickNum;
        }else {
            clickNum = 0;
            startTime = System.currentTimeMillis();
        }
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq" + "" + "" + "" + "" + "" + "" + "" +
                ".com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            showT(getString(R.string.join_QQ_error));
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    private void shareApp() {
        final Bitmap bitmap = encodeAsBitmap(MyApp.mAppShare);

        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(bitmap);

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)//
                .setTitle(R.string.ask_friend_hint_msg)//
                .setView(imageView)//
                .setNegativeButton(R.string.cancel, null)//
                .setPositiveButton(R.string.ask_friend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file = new File(MyApp.getAppPath(), "appQR_" + System.currentTimeMillis() + ".png");
                        try {
                            if (file.exists()) {
                                file.delete();
                            }

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), R.string.qr_save_success, Toast.LENGTH_SHORT).show();
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            Intent intent = new Intent();
                            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                            intent.setAction(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.not_weixin, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels / 2;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, width, width);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }

        return bitmap;
    }
}
