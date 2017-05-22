package com.lyl.myallforyou.network;


import com.lyl.myallforyou.network.api.NeihanApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lyl on 2017/5/9.
 */
public class Network {

    private static String URL_NEIHAN = "http://is.snssdk.com/";

    private static final int DEFAULT_TIMEOUT = 15;

    public static OkHttpClient httpClient;
    private static NeihanApi neihanApi;

    static {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

//        if ("dev".equals(BuildConfig.Environment)) {
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            httpClientBuilder.addInterceptor(logging);
//        }
        httpClient = httpClientBuilder.build();
    }

    private static Retrofit getRetrofit(String url) {
        return new Retrofit.Builder()//
                .client(httpClient)//
                .baseUrl(url)//
                .addConverterFactory(GsonConverterFactory.create())//
                .build();
    }

    public static NeihanApi getNeihanApi() {
        if (neihanApi == null) {
            neihanApi = getRetrofit(URL_NEIHAN).create(NeihanApi.class);
        }
        return neihanApi;
    }
}
