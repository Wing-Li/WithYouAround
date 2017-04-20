package com.lyl.myallforyou.ui.qrbind;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;
import com.lyl.myallforyou.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrShareActivity extends BaseActivity {

    @Bind(R.id.qr_share_img)
    ImageView qrShareImg;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_share);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.my_device);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        // TODO 这里应该向服务器检查一下，自己在不在服务里里面

        Bitmap bitmap = encodeAsBitmap(AppUtils.getUUID());
        qrShareImg.setImageBitmap(bitmap);
    }


    private int getSrceenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        int width = getSrceenWidth() / 2;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, width, width);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }

        return bitmap;
    }
}
