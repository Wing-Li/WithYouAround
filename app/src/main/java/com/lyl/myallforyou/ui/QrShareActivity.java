package com.lyl.myallforyou.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrShareActivity extends AppCompatActivity {

    @Bind(R.id.qr_share_img)
    ImageView qrShareImg;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_share);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        Bitmap bitmap = encodeAsBitmap(AppUtils.getUUID(QrShareActivity.this));
        qrShareImg.setImageBitmap(bitmap);
    }


    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 450, 450);
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
