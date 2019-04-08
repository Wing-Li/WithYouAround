package com.lyl.myallforyou.ui.qrbind;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.ui.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;

public class QrShareActivity extends BaseActivity {

    @Bind(R.id.qr_share_img)
    ImageView qrShareImg;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.qr_share_uuid)
    TextView qrShareUuid;
    @Bind(R.id.qr_share_uuid_layout)
    LinearLayout qrShareUuidLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_share);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.my_device);
        setSupportActionBar(toolbar);
        setBackUI(toolbar);

        // TODO 这里应该向服务器检查一下，自己在不在服务里里面

        qrShareUuid.setText(uuid);
        qrShareUuidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("uuid",uuid));
                showT(getString(R.string.copy_success));
            }
        });

        Bitmap bitmap = encodeAsBitmap(uuid);
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
