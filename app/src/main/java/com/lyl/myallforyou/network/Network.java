package com.lyl.myallforyou.network;


import com.lyl.myallforyou.network.api.NeihanApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lyl on 2017/5/9.
 */
public class Network {

    private static String URL_NEIHAN = "http://is.snssdk.com/neihan/stream/mix/v1/";

    private static final int DEFAULT_TIMEOUT = 30;

    private static OkHttpClient.Builder httpClientBuilder;
    private static NeihanApi neihanApi;

    private static void initOkHttp() {
        httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    private static Retrofit getRetrofit(String url) {
        if (httpClientBuilder == null) {
            initOkHttp();
        }

        return new Retrofit.Builder()//
                .client(httpClientBuilder.build())//
                .baseUrl(url)//
                .addConverterFactory(GsonConverterFactory.create())//
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
                .build();
    }

    public static NeihanApi getNeihanApi() {
        if (neihanApi == null) {
            neihanApi = getRetrofit(URL_NEIHAN).create(NeihanApi.class);
        }
        return neihanApi;
    }
}
