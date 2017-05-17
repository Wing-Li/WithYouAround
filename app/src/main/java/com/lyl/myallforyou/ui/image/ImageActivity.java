package com.lyl.myallforyou.ui.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.data.NhEssay;
import com.lyl.myallforyou.view.HackyViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageActivity extends BaseImageActivity {

    @Bind(R.id.image_viewPager)
    HackyViewPager imageViewPager;
    @Bind(R.id.image_btn)
    Button imageBtn;

    private ImageAdapter mImageAdapter;

    private List<NhEssay.DataBeanX.DataBean.GroupBean.LargeImageListBean> mImageListBeanList;
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
        mPostion = intent.getIntExtra(ConstantIntent.IMAGE_LIST_POSTION, 0);
        mImageListBeanList = (List<NhEssay.DataBeanX.DataBean.GroupBean.LargeImageListBean>) intent.getSerializableExtra(ConstantIntent
                .IMAGE_LIST);
    }

    private void initView() {
        mImageAdapter = new ImageAdapter(mContext, mImageListBeanList);
        imageViewPager.setAdapter(mImageAdapter);
        imageViewPager.setOffscreenPageLimit(2);
        imageViewPager.setCurrentItem(mPostion);

        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download(imageBtn, mImageListBeanList.get(mPostion).getUrl(), false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
