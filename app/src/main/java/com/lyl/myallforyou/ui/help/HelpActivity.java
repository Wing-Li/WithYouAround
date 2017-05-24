package com.lyl.myallforyou.ui.help;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.help);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);
    }
}
