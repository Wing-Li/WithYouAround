package com.lyl.myallforyou.ui.qrbind;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.client.android.Intents;
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
        toolbar.setTitle(R.string.bind);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_qr_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_qr_scan_input:
                setContent();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setContent() {
        // 获取到剪贴板的内容
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        ClipData.Item item = primaryClip.getItemAt(0);
        CharSequence copy = item.getText();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText edt = (EditText) view.findViewById(R.id.dialog_edt);
        edt.setMaxLines(2);
        edt.setText(copy);
        builder.setTitle(R.string.input_user_id);
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

                Intent intent = new Intent();
                intent.putExtra(Intents.Scan.RESULT, edtStr);
                setResult(Activity.RESULT_OK, intent);
                finish();

                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
