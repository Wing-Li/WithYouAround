package com.lyl.myallforyou.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.ImgUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LongImageActivity extends BaseActivity {

    @Bind(R.id.long_image)
    SubsamplingScaleImageView longImage;

    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_long_image);
        ButterKnife.bind(this);

        getPrameter();

        ImgUtils.getBitmap(mContext, mUrl, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                longImage.setImage(ImageSource.bitmap(bitmap));
            }
        });
    }

    private void getPrameter() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(ConstantIntent.LONG_IMAGE_URL);
    }
}
