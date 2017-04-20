package com.lyl.myallforyou.ui.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.about_version)
    TextView aboutVersion;
    @Bind(R.id.about_email)
    TextView aboutEmail;


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

    }
}
