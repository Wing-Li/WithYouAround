package com.lyl.myallforyou.ui.qrbind;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrScanActivity extends BaseActivity {

    @Bind(R.id.dbv_custom)
    DecoratedBarcodeView dbvCustom;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private CaptureManager captureManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, dbvCustom);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

    }


    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return dbvCustom.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
