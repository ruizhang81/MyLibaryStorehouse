package com.lib_http;


import android.content.Context;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitFactory {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context, String BaseUrl,
                                       HashMap<String, String> baseHeader,
                                       HashMap<String, String> baseParam) {
        if (retrofit == null || !BaseUrl.equals(retrofit.baseUrl().host())) {
            synchronized (RetrofitFactory.class) {
                if (retrofit == null || !BaseUrl.equals(retrofit.baseUrl().host()) ) {
                    OkHttpClient client = OkHttp.createOkhttpClient(context, baseHeader, baseParam);

                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(BaseUrl)
                            .client(client)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }



}
