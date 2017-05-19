package com.lyl.myallforyou.ui.main;

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
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.data.event.MainEvent;
import com.lyl.myallforyou.service.DeviceInfoService;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.ui.about.AboutActivity;
import com.lyl.myallforyou.ui.essay.NhEassayActivity;
import com.lyl.myallforyou.ui.feedback.FeedbackActivity;
import com.lyl.myallforyou.ui.qrbind.QrScanActivity;
import com.lyl.myallforyou.ui.qrbind.QrShareActivity;
import com.lyl.myallforyou.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lyl.myallforyou.constants.Constans.SP_MY_NAME;
import static com.lyl.myallforyou.constants.Constans.SP_MY_SGIN;
import static com.lyl.myallforyou.constants.Constans.USER_FAMILYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYSGIN;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private ImageView mIcon;
    private TextView mName;
    private TextView mSgin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawerLayout();
        initToolbar();
        initFloating();
        initMainContent();

        Intent intent = new Intent(this, DeviceInfoService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        String myName = (String) SPUtil.get(mContext, SP_MY_NAME, "");
        if (TextUtils.isEmpty(myName)) {
            setName();
        }
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void initFloating() {
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
    }


    private void initMainContent() {
        MainFragment mainFragment = MainFragment.newInstance(1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, mainFragment);
        transaction.commit();
    }


    // ============================================== ↓扫描二维码绑定用户↓ ==============================================


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (TextUtils.isEmpty(intentResult.getContents())) {
                Toast.makeText(this, R.string.scan_error, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.scan_success, Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String scanResult = intentResult.getContents();
                setNameNote(scanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 扫描完之后，先设置备注
     */
    private void setNameNote(final String familyUuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.dialog_namenote_title);
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
                String edtStr = edt.getText().toString().trim();
                final String str = checkStr(edtStr, 6, false);
                if (TextUtils.isEmpty(str)) return;

                bindUser(familyUuid, str);

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    /**
     * 绑定扫描到的数据
     *
     * @param familyUuid 对方的UUID
     * @param nameNote   给对方的备注
     */
    private void bindUser(final String familyUuid, final String nameNote) {
        // 查询对方的数据
        AVQuery<AVObject> familyQuery = new AVQuery<>(Constans.TABLE_USER_INFO);
        familyQuery.whereContains(USER_MYID, familyUuid);
        familyQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject familyObject, AVException e) {
                if (e == null && familyObject != null) {
                    // 查询自己的数据
                    AVQuery<AVObject> myQuery = new AVQuery<>(Constans.TABLE_USER_INFO);
                    myQuery.whereContains(USER_MYID, uuid);
                    myQuery.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject myObject, AVException e) {
                            if (e == null && myObject != null) {
                                ArrayList<AVObject> todos = new ArrayList<AVObject>();

                                // 把对方加入我的表里
                                String myFamily = myObject.getString(Constans.USER_FAMILYID);
                                AVObject my = addFamilyToMy(objId, familyUuid, myFamily);
                                todos.add(my);

                                // 把我加到对方的表里
                                final String familyObjId = familyObject.getObjectId();
                                String familyId = (String) familyObject.get(Constans.USER_FAMILYID);
                                AVObject family = addFamilyToMy(familyObjId, uuid, familyId);
                                todos.add(family);

                                AVObject.saveAllInBackground(todos, new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            // 查询本地的用户
                                            UserInfo userInfo = null;
                                            try {
                                                userInfo = new UserInfo();
                                                userInfo.setObjid(familyObjId);
                                                userInfo.setUuid(familyUuid);
                                                userInfo.setName(familyObject.getString(Constans.USER_MYNAME));
                                                userInfo.setNameNote(nameNote);
                                                userInfo.setSign(familyObject.getString(Constans.USER_MYSGIN));
                                                MyApp.liteOrm.insert(userInfo, ConflictAlgorithm.Abort);
                                            } catch (Exception ex) {
                                                MyApp.liteOrm.update(userInfo);
                                            }

                                            EventBus.getDefault().post(new MainEvent());
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else {
                    showT(R.string.not_my_object_id);
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
    private AVObject addFamilyToMy(String my, String family, String myFamilyId) {
        AVObject userInfo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, my);
        String saveFamilyid;
        if (TextUtils.isEmpty(myFamilyId) || !myFamilyId.contains(family)) {
            if (TextUtils.isEmpty(myFamilyId)) {
                saveFamilyid = family;
            } else {
                saveFamilyid = myFamilyId + "," + family;
            }
            userInfo.put(USER_FAMILYID, saveFamilyid);
        }
        return userInfo;
    }

    // ============================================== ↑扫描二维码绑定用户↑ ==============================================

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
        mSgin = (TextView) view.findViewById(R.id.nav_header_content);

        String spName = (String) SPUtil.get(mContext, Constans.SP_MY_NAME, "");
        if (!TextUtils.isEmpty(spName)) {
            mName.setText(spName);
        }
        String spSgin = (String) SPUtil.get(mContext, Constans.SP_MY_SGIN, "");
        if (!TextUtils.isEmpty(spSgin)) {
            mSgin.setText(spSgin);
        }

        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName();
            }
        });
        mSgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContent();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                if (id == R.id.nav_nheassay) {
                    intent = new Intent(mContext, NhEassayActivity.class);
                    intent.putExtra(NhEassayActivity.CONTENT_TYPE, NhEassayActivity.CONTENT_TYPE_ESSAY);
                } else if (id == R.id.nav_nhimage) {
                    intent = new Intent(mContext, NhEassayActivity.class);
                    intent.putExtra(NhEassayActivity.CONTENT_TYPE, NhEassayActivity.CONTENT_TYPE_IMAGE);
                } else if (id == R.id.nav_nhvideo) {

                } else if (id == R.id.nav_feedback) {
                    intent = new Intent(mContext, FeedbackActivity.class);
                } else if (id == R.id.nav_about) {
                    intent = new Intent(mContext, AboutActivity.class);
                }
                if (intent != null) {
                    startActivity(intent);
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
                String edtStr = edt.getText().toString().trim();
                final String str = checkStr(edtStr, 6, false);
                if (TextUtils.isEmpty(str)) return;

                AVObject todo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, objId);
                todo.put(Constans.USER_MYNAME, str);
                todo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
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
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    /**
     * 保存签名到服务器
     */
    private void setContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        builder.setTitle(R.string.user_sign);
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
                String edtStr = edt.getText().toString().trim();
                final String str = checkStr(edtStr, 20, true);
                if (TextUtils.isEmpty(str)) return;

                AVObject todo = AVObject.createWithoutData(Constans.TABLE_USER_INFO, objId);
                todo.put(USER_MYSGIN, str);
                todo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showT(R.string.save_success);
                            SPUtil.put(mContext, SP_MY_SGIN, str);
                            mSgin.setText(str);
                        } else {
                            showT(R.string.save_fail);
                        }
                    }
                });

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    // ============================================== ↑侧边栏↑ ==============================================


    /**
     * 检查输入的字符串
     *
     * @param str           被检查的内容
     * @param length        要求的长度
     * @param isSpecialChar 是否可以输入特殊字符
     * @return 符合要求则返回原字符串；否则，返回 空字符串
     */
    private String checkStr(String str, int length, boolean isSpecialChar) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(mContext, R.string.not_empty, Toast.LENGTH_SHORT).show();
            return "";
        }
        if (str.length() > length) {
            Toast.makeText(mContext, getString(R.string.length_error, length), Toast.LENGTH_SHORT).show();
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
