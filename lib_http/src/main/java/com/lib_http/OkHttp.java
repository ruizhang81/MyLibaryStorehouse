package com.lib_http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zhangrui on 17/1/2.
 */

public class OkHttp {

    private final static long connectTimeout = 60l;
    private final static long readTimeout = 60l;
    private final static long writeTimeout = 60l;

    public static OkHttpClient createOkhttpClient(Context context, HashMap<String, String> baseHeader, HashMap<String, String> baseParam) {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(addHeaderInterceptor(baseHeader)) // 头添加
                .addInterceptor(addQueryParameterInterceptor(baseParam))  //参数添加
                .addInterceptor(addhttpLoggingInterceptor()) //日志,所有的请求响应度看到
                .cache(createCache(context))  //添加缓存
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
        return client;
    }

    /**
     * 设置打印拦截器
     */
    private static Interceptor addhttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("http", "Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    /**
     * 设置头
     *
     * @param baseHeader
     */
    private static Interceptor addHeaderInterceptor(final HashMap<String, String> baseHeader) {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                if (baseHeader != null && baseHeader.keySet() != null) {
                    Iterator<String> iter = baseHeader.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = baseHeader.get(key);
                        requestBuilder.addHeader(key, value);
                    }
                }
//                requestBuilder.addHeader("Connection", "close");
                requestBuilder.method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 设置公共参数
     *
     * @param baseParam
     */
    private static Interceptor addQueryParameterInterceptor(final HashMap<String, String> baseParam) {
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl.Builder builder = originalRequest.url().newBuilder();
                if (baseParam != null && baseParam.keySet() != null) {
                    Iterator<String> iter = baseParam.keySet().iterator();
                    StringBuilder sb = new StringBuilder();
                    boolean isFirst = false;
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = baseParam.get(key);
                        builder.addQueryParameter(key, value);
                        if (!isFirst) {
                            sb.append("&");
                        }
                        sb.append(key);
                        sb.append("=");
                        sb.append(value);
                    }
                    encryption(builder, baseParam, sb.toString());
                }
                HttpUrl modifiedUrl = builder.build();
                Request request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        return addQueryParameterInterceptor;
    }

    //加密
    private static void encryption(HttpUrl.Builder builder, HashMap<String, String> baseParam, String string) {
        if (!TextUtils.isEmpty(string) && baseParam != null) {
            if ("MD5".equals(baseParam.get("sign_type"))) {
                builder.addQueryParameter("sign", md5(string));
            }
        }
    }

    //md5
    private static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Cache createCache(Context context) {
        //设置 请求的缓存的大小跟位置
        File cacheFile = new File(context.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb 缓存的大小
        return cache;

    }

}
