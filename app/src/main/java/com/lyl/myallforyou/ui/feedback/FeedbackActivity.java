package com.lyl.myallforyou.ui.feedback;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.Constans;
import com.lyl.myallforyou.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.feedback_edt)
    EditText feedbackEdt;
    @Bind(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        initView();
    }


    private void initView() {
        toolbar.setTitle(R.string.feedback);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback();
            }
        });
    }


    private void sendFeedback() {
        new AlertDialog.Builder(this)//
                .setTitle(R.string.hint)//
                .setMessage(R.string.put_ask)//
                .setNegativeButton(R.string.cancel, null)//
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = feedbackEdt.getText().toString().trim();
                        AVObject todoFolder = new AVObject(Constans.TABLE_FEEDBACK);// 构建对象
                        todoFolder.put(Constans.FEEDBACK_UUID, uuid);
                        todoFolder.put(Constans.FEEDBACK_SUG, str);
                        todoFolder.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    showT(R.string.upload_success);
                                    finish();
                                }else {
                                    showT(R.string.upload_error);
                                }
                            }
                        });
                    }
                }).create().show();
    }
}
