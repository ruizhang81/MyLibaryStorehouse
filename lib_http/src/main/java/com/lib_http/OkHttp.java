package com.lib_http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

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


                Request oldRequest = chain.request();
                //生成新的请求体
                HttpUrl.Builder newBuilder = oldRequest.url().newBuilder()
                        .scheme(oldRequest.url().scheme())
                        .host(oldRequest.url().host());
                // 向新的请求体里添加公共参数，然后生成公共参数加密串
                if (baseParam != null && baseParam.keySet() != null) {
                    Iterator<String> iter = baseParam.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = baseParam.get(key);
                        newBuilder.addQueryParameter(key, value);
                        Log.e("xxx"," base add param key= "+key+" value="+value);
                    }
                }
                // 新的请求
                Request newRequest = oldRequest.newBuilder()
                        .method(oldRequest.method(), oldRequest.body())
                        .url(newBuilder.build())
                        .build();

                return chain.proceed(newRequest);


                /**
                Request oldRequest = chain.request();

                //生成新的请求体
                HttpUrl.Builder newBuilder = oldRequest.url().newBuilder()
                        .scheme(oldRequest.url().scheme())
                        .host(oldRequest.url().host());

                //初始化加密串
                StringBuilder sb = new StringBuilder();
                boolean notFirst = false;

                // 向新的请求体里添加公共参数，然后生成公共参数加密串
                if (baseParam != null && baseParam.keySet() != null) {
                    Iterator<String> iter = baseParam.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = baseParam.get(key);
                        newBuilder.addQueryParameter(key, value);
                        if (notFirst) {
                            sb.append("&");
                        }
                        sb.append(key);
                        sb.append("=");
                        sb.append(value);
                        notFirst = true;
                        Log.e("xxx"," base add param key= "+key+" value="+value);
                    }
                }

                //生成动态参数加密串
                if (oldRequest.body() instanceof FormBody) {
                    FormBody oidFormBody = (FormBody) oldRequest.body();
                    for (int i = 0; i < oidFormBody.size(); i++) {
                        if (notFirst) {
                            sb.append("&");
                        }
                        String key = oidFormBody.encodedName(i);
                        String value = oidFormBody.encodedValue(i);
                        sb.append(key);
                        sb.append("=");
                        sb.append(value);
                        Log.e("xxx","new add param key= "+key+" value="+value);
                        notFirst = true;
                    }
                }else{
                }
                encryption(newBuilder, baseParam, sb.toString());


                // 新的请求
                Request newRequest = oldRequest.newBuilder()
                        .method(oldRequest.method(), oldRequest.body())
                        .url(newBuilder.build())
                        .build();

                return chain.proceed(newRequest);
                 **/
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
