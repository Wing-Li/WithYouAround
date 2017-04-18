package com.lyl.myallforyou.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.service.DeviceInfoService;
import com.lyl.myallforyou.utils.SPUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lyl.myallforyou.constants.Constans.SP_MY_NAME;
import static com.lyl.myallforyou.constants.Constans.USER_FAMILYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYNAME;

public class MainActivity extends BaseActivity {

    private ImageView mIcon;
    private TextView mName;
    private TextView mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawerLayout();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initMainContent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 初始化扫描
                new IntentIntegrator(MainActivity.this)//
                        .setOrientationLocked(false)//
                        // 设置自定义的activity是CustomActivity
                        .setCaptureActivity(QrScanActivity.class) //
                        .initiateScan();//
            }
        });

        Intent intent = new Intent(this, DeviceInfoService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


    private void initMainContent() {
        MainFragment mainFragment = MainFragment.newInstance(1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, mainFragment);
        transaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String scanResult = intentResult.getContents();
                bindUser(scanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 绑定扫描到的数据
     *
     * @param familyUuid 对方的UUID
     */
    private void bindUser(final String familyUuid) {
        AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
        query.whereContains(USER_MYID, familyUuid);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null && avObject != null) {
                    // 把对方加入我的表里
                    String myFamily = (String) SPUtil.get(mContext, Constans.SP_FAMILY_ID, "");
                    addFamilyToMy(objId, familyUuid, myFamily);

                    // 把我加到对方的表里
                    String familyObjId = avObject.getObjectId();
                    String familyId = (String) avObject.get(Constans.USER_FAMILYID);
                    addFamilyToMy(familyObjId, uuid, familyId);
                } else {
                    Toast.makeText(mContext, R.string.not_my_object_id, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


    /**
     * 将 family 与 my 绑定
     *
     * @param my         我的 objectId
     * @param family     对方 uuid
     * @param myFamilyId 与我绑定的人id
     */
    private void addFamilyToMy(String my, String family, String myFamilyId) {
        AVObject userInfo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, my);
        if (TextUtils.isEmpty(myFamilyId) || !myFamilyId.contains(family)) {
            if (TextUtils.isEmpty(myFamilyId)) {
                userInfo.put(USER_FAMILYID, family);
            } else {
                userInfo.put(USER_FAMILYID, myFamilyId + "," + family);
            }
            userInfo.saveInBackground();
        }
        userInfo = null;
    }

    // ============================================== ↓侧边栏↓ ==============================================


    public void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);

        mIcon = (ImageView) view.findViewById(R.id.nav_header_icon);
        mName = (TextView) view.findViewById(R.id.nav_header_name);
        mContent = (TextView) view.findViewById(R.id.nav_header_content);

        String spName = (String) SPUtil.get(mContext, Constans.SP_MY_NAME, "");
        if (!TextUtils.isEmpty(spName)) {
            mName.setText(spName);
        }
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_camera) {

                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    /**
     * 保存名字到服务端
     */
    private void setName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.user_name);
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String str = edt.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(mContext, R.string.name_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (str.length() > 6) {
                    Toast.makeText(mContext, R.string.name_length_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                String zhengze = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
                Pattern pattern = Pattern.compile(zhengze);
                Matcher matcher = pattern.matcher(str);
                if (!matcher.matches()) {
                    Toast.makeText(mContext, R.string.name_style_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                AVObject todo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, objId);
                todo.put(USER_MYNAME, str);
                todo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            showT(R.string.save_success);
                            SPUtil.put(mContext, SP_MY_NAME, str);
                            mName.setText(str);
                        } else {
                            showT(R.string.save_fail);
                        }
                    }
                });

                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    // ============================================== ↑侧边栏↑ ==============================================

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }


        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_my_qr) {
            Intent intent = new Intent(MainActivity.this, QrShareActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
