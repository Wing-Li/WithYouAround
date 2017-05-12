package com.lyl.myallforyou.ui.image;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.view.HackyViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {

    @Bind(R.id.image_viewPager)
    HackyViewPager imageViewPager;

    private ImageAdapter mImageAdapter;

    private ArrayList<String> mUrlList;
    private int mPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        getParameter();

        initView();
    }

    private void getParameter() {
        Intent intent = getIntent();
        mUrlList = intent.getStringArrayListExtra(ConstantIntent.IMAGE_LIST_URL);
        mPostion = intent.getIntExtra(ConstantIntent.IMAGE_LIST_POSTION, 0);
    }

    private void initView() {
        mImageAdapter = new ImageAdapter(mContext, mUrlList);
        imageViewPager.setAdapter(mImageAdapter);
        imageViewPager.setOffscreenPageLimit(2);
        imageViewPager.setCurrentItem(mPostion);
    }
}
