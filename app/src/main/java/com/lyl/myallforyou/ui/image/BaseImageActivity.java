package com.lyl.myallforyou.ui.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.lyl.myallforyou.MyApp;
import com.lyl.myallforyou.R;
import com.lyl.myallforyou.network.Network;
import com.lyl.myallforyou.ui.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lyl on 2017/5/16.
 */

public class BaseImageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void download(final View view, String fileUrl, final boolean isGif) {
        Toast.makeText(mContext, R.string.download_running, Toast.LENGTH_SHORT).show();
        view.setVisibility(View.GONE);

        Call<ResponseBody> responseBodyCall = Network.getNeihanApi().downloadFileWithDynamicUrlSync(fileUrl);
        Call<ResponseBody> clone = responseBodyCall.clone();
        clone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String imgName = "";
                    if (isGif) {
                        imgName = "neihan_" + System.currentTimeMillis() + ".gif";
                    } else {
                        imgName = "neihan_" + System.currentTimeMillis() + ".jpeg";
                    }

                    File imgFile = new File(MyApp.getAppPath() + File.separator + imgName);
                    boolean toDisk = writeResponseBodyToDisk(response.body(), imgName, imgFile);
                    if (toDisk) {
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imgFile)));
                        Toast.makeText(getApplicationContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String imgName, File imgFile) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[1024];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(imgFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
