package com.lyl.myallforyou.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.constants.ConstantIntent;
import com.lyl.myallforyou.utils.ImgUtils;
import com.lyl.myallforyou.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpecialImageActivity extends BaseImageActivity {


    @Bind(R.id.long_image)
    SubsamplingScaleImageView longImage;
    @Bind(R.id.gif_image)
    PhotoView gifImage;
    @Bind(R.id.image_btn)
    Button imageBtn;

    private String mUrl;
    private String mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special_image);
        ButterKnife.bind(this);

        getPrameter();
        initView();
    }

    private void initView() {
        if (mType.equals(ConstantIntent.SPECIAL_IMAGE_GIF)) {
            longImage.setVisibility(View.GONE);
            gifImage.setVisibility(View.VISIBLE);

            ImgUtils.loadGif(mContext, mUrl, gifImage);
        } else if (mType.equals(ConstantIntent.SPECIAL_IMAGE_NORMAL)) {
            longImage.setVisibility(View.GONE);
            gifImage.setVisibility(View.VISIBLE);

            ImgUtils.load(mContext, mUrl, gifImage);
        } else if (mType.equals(ConstantIntent.SPECIAL_IMAGE_LONG)) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            longImage.setScaleAndCenter(1f, new PointF(displayMetrics.widthPixels, displayMetrics.widthPixels));
            longImage.setVisibility(View.VISIBLE);
            gifImage.setVisibility(View.GONE);

            ImgUtils.getBitmap(mContext, mUrl, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                    longImage.setImage(ImageSource.bitmap(bitmap));
                }
            });
        }

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mType.equals(ConstantIntent.SPECIAL_IMAGE_GIF)) {
                    download(imageBtn, mUrl, true);
                } else {
                    download(imageBtn, mUrl, false);
                }
            }
        });
    }

    private void getPrameter() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(ConstantIntent.SPECIAL_IMAGE_URL);
        mType = intent.getStringExtra(ConstantIntent.SPECIAL_IMAGE_TYPE);
        LogUtils.d("显示单张图片：", mType + " : " + mUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrl = null;
        longImage = null;
        gifImage.setImageBitmap(null);
        gifImage = null;
    }
}
