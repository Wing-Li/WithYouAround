package com.lyl.myallforyou.ui.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.data.UserInfo;
import com.lyl.myallforyou.data.event.MainEvent;
import com.lyl.myallforyou.im.IMCallBack;
import com.lyl.myallforyou.im.IMGetUserInfoCallBack;
import com.lyl.myallforyou.im.IMutils;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.ui.about.AboutActivity;
import com.lyl.myallforyou.ui.about.SettingActivity;
import com.lyl.myallforyou.ui.essay.NhEassayActivity;
import com.lyl.myallforyou.ui.help.HelpActivity;
import com.lyl.myallforyou.ui.qrbind.QrScanActivity;
import com.lyl.myallforyou.ui.qrbind.QrShareActivity;
import com.lyl.myallforyou.ui.userinfo.UserInfoActivity;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.MyUtils;
import com.lyl.myallforyou.utils.NetUtil;
import com.lyl.myallforyou.utils.SPUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import static com.lyl.myallforyou.constants.Constans.SP_MY_NAME;
import static com.lyl.myallforyou.constants.Constans.SP_MY_SGIN;
import static com.lyl.myallforyou.constants.Constans.USER_FAMILYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYID;
import static com.lyl.myallforyou.constants.Constans.USER_MYSGIN;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    public static final int IMAGE_PICKER = 1008;

    private ImageView mBg;
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

        String myName = (String) SPUtil.get(mContext, SP_MY_NAME, "");
        if (TextUtils.isEmpty(myName)) {
            setName();
        }

        initImagePicker();

        initIm();
    }

    /**
     * 初始化极光推送服务器
     */
    private void initIm() {
        // 登陆极光服务器，登陆成功后跳转主页面
        String currUuid = (String) SPUtil.get(mContext, Constans.SP_UUID, "");
        IMutils.loginJG(currUuid, IMutils.password, new IMCallBack() {
            @Override
            public void onSuccess(int code, String msg) {
                // 在刚初始化结束时，想要请求头像，只能通过这个方法。
                // 刚初始化结局 IMutils.getMyInfo() 请求头像可能是空。
                IMutils.getUserInfo(uuid, new IMGetUserInfoCallBack() {
                    @Override
                    public void onSuccess(cn.jpush.im.android.api.model.UserInfo myInfo) {
                        if (myInfo != null) {
                            File avatarFile = myInfo.getAvatarFile();
                            //登陆成功,如果用户有头像就把头像存起来,没有就设置null
                            if (avatarFile != null && avatarFile.exists()) {
                                SPUtil.put(mContext, Constans.SP_MY_ICON, avatarFile.getAbsolutePath());
                                ImgUtils.loadCircle(mContext, Uri.fromFile(avatarFile), mIcon);
                            }

                            // 检查名字与服务器名字是否一致，不一致则更改服务器名字
                            String myName = (String) SPUtil.get(mContext, SP_MY_NAME, "");
                            if (!TextUtils.isEmpty(myName)) {
                               if (myName.equals(myInfo.getNickname())) {
                                   IMutils.updateUserName(myName, null);
                               }
                            }
                        }
                    }
                });
            }

            @Override
            public void onFail(int code, String msg) {
                showT(getString(R.string.login_jg_fail));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mName != null) {
            String spName = (String) SPUtil.get(mContext, Constans.SP_MY_NAME, "");
            if (!TextUtils.isEmpty(spName)) {
                mName.setText(spName);
            }
        }
        if (mSgin != null) {
            String spSgin = (String) SPUtil.get(mContext, Constans.SP_MY_SGIN, "");
            if (!TextUtils.isEmpty(spSgin)) {
                mSgin.setText(spSgin);
            }
        }
        if (mIcon != null) {
            String spSgin = (String) SPUtil.get(mContext, Constans.SP_MY_ICON, "");
            if (TextUtils.isEmpty(spSgin)) {
                cn.jpush.im.android.api.model.UserInfo myInfo = IMutils.getMyInfo();
                if (myInfo != null) {
                    File avatarFile = myInfo.getAvatarFile();
                    if (avatarFile != null && avatarFile.exists()) {
                        SPUtil.put(getApplicationContext(), Constans.SP_MY_ICON, avatarFile);
                        ImgUtils.loadCircle(mContext, Uri.fromFile(avatarFile), mIcon);
                    }
                }
            } else {
                ImgUtils.loadCircle(mContext, spSgin, mIcon);
            }
        }
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
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
        MainFragment mainFragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, mainFragment);
        transaction.commit();
    }


    // ============================================== ↓扫描二维码绑定用户↓ ==============================================


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 修改头像
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker
                        .EXTRA_RESULT_ITEMS);
                ImageItem image = images.get(0);
                final File file = new File(image.path);

                IMutils.updateUserIcon(file, new IMCallBack() {
                    @Override
                    public void onSuccess(int code, String msg) {
                        ImgUtils.loadCircle(mContext, Uri.fromFile(file), mIcon);
                        EventBus.getDefault().post(new MainEvent());
                        SPUtil.put(getApplicationContext(), Constans.SP_MY_ICON, file);
                        showT(R.string.save_success);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        showT(R.string.save_fail);
                    }
                });
            }
            return;
        }
        // 获取扫描的二维码
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (TextUtils.isEmpty(intentResult.getContents())) {
                Toast.makeText(this, R.string.scan_error, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.scan_success, Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                final String scanResult = intentResult.getContents();

                // 查看扫描到的二维码id 在不在 服务端数据库内
                AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
                query.whereEqualTo(Constans.USER_MYID, scanResult);
                query.countInBackground(new CountCallback() {
                    @Override
                    public void done(int i, AVException e) {
                        if (e == null && i > 0) {
                            setNameNote(scanResult);
                        } else {
                            showT(getString(R.string.other_id_error));
                        }
                    }
                });
            }
            return;
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
                final String str = MyUtils.checkStr(mContext, edtStr, 6, false);
                if (TextUtils.isEmpty(str)) return;

                bindUser(familyUuid, str);

                dialogInterface.dismiss();
            }
        });
        try {
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
        }
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
        familyQuery.whereEqualTo(USER_MYID, familyUuid);
        familyQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject familyObject, AVException e) {
                if (e == null && familyObject != null) {
                    // 查询自己的数据
                    AVQuery<AVObject> myQuery = new AVQuery<>(Constans.TABLE_USER_INFO);
                    myQuery.whereEqualTo(USER_MYID, uuid);
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
                            } else {
                                showT(R.string.not_my_object_id);
                            }
                        }
                    });

                } else {
                    showT(R.string.other_id_error);
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);

        mBg = (ImageView) view.findViewById(R.id.nav_header_bg);
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
        String iconPath = (String) SPUtil.get(mContext, Constans.SP_MY_ICON, "");
        File avatarFile = new File(iconPath != null ? iconPath : "");
        if (avatarFile.exists()) {
            ImgUtils.loadCircle(mContext, Uri.fromFile(avatarFile), mIcon);
        }

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserInfo();
            }
        });
        mIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setIcon();
                return true;
            }
        });
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
                if (id == R.id.nav_nheassay) { // 内涵段子
                    intent = new Intent(mContext, NhEassayActivity.class);
                    intent.putExtra(NhEassayActivity.CONTENT_TYPE, NhEassayActivity.CONTENT_TYPE_ESSAY);
                } else if (id == R.id.nav_nhimage) { // 内涵图片
                    if (NetUtil.isWifi(getApplicationContext())) {
                        openNhEassayImage();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext)//
                                .setTitle(R.string.hint)//
                                .setMessage(R.string.use_flux)//
                                .setPositiveButton(R.string.cancel, null)//
                                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        openNhEassayImage();
                                        dialogInterface.dismiss();
                                    }
                                }).create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                } else if (id == R.id.nav_nhvideo) {

                } else if (id == R.id.nav_ask) { // 邀请好友
                    MyUtils.shareApp(MainActivity.this);
                } else if (id == R.id.nav_help) { // 使用帮助
                    intent = new Intent(mContext, HelpActivity.class);
                } else if (id == R.id.nav_setting) { // 设置
                    startActivity(new Intent(mContext, SettingActivity.class));
                    finish();
                } else if (id == R.id.nav_about) { // 关于
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

    private void openNhEassayImage() {
        Intent intent = new Intent(mContext, NhEassayActivity.class);
        intent.putExtra(NhEassayActivity.CONTENT_TYPE, NhEassayActivity.CONTENT_TYPE_IMAGE);
        startActivity(intent);
    }

    private void setUserInfo() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
        mDrawerLayout.closeDrawers();
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
                final String str = MyUtils.checkStr(mContext, edtStr, 6, false);
                if (TextUtils.isEmpty(str)) return;

                // 通过 UUID 找到用户
                AVQuery<AVObject> query = new AVQuery<>(Constans.TABLE_USER_INFO);
                query.whereContains(Constans.USER_MYID, uuid);
                query.whereStartsWith(Constans.USER_MYID, uuid);
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null && avObject != null) {
                            String objectId = avObject.getObjectId();
                            if (!objectId.equals(objId)) {
                                objId = objectId;
                                SPUtil.put(mContext, Constans.SP_OBJ_ID, objectId);
                            }

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
                        }
                    }
                });

                // 将昵称上传到聊天服务器
                IMutils.updateUserName(str, null);

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
                final String str = MyUtils.checkStr(mContext, edtStr, 20, true);
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

    // ============================================== ↓图片选择↓ ==============================================

    private void setIcon() {
        Intent intent = new Intent(getApplicationContext(), ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    static class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Glide.with(activity).load(Uri.fromFile(new File(path)))//
                    .placeholder(R.drawable.gary_bg)//
                    .error(R.drawable.gary_bg)//
                    .override(width, height)//
                    .centerCrop()//
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//
                    .into(imageView);
        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            Glide.with(activity).load(Uri.fromFile(new File(path)))//
                    .placeholder(R.drawable.gary_bg)//
                    .error(R.drawable.gary_bg)//
                    .override(width, height)//
                    .centerCrop()//
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }

    // ============================================== ↑图片选择↑ ==============================================
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

    private long statTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果侧边栏开着，就把他关掉
            if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawers();
            } else {
                // 点击两下返回
                if (System.currentTimeMillis() - statTime > 2000) {
                    Toast.makeText(getApplicationContext(), "再次点击关闭程序", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                statTime = System.currentTimeMillis();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static final String TAG_EXIT = "exit";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
