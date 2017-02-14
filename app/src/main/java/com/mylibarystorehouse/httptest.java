package com.mylibarystorehouse;

import android.util.Log;

import com.lib_http.RetrofitFactory;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangrui on 17/1/7.
 */

public class httptest {



    public static void  run(){
        getCityData();
    }

    private final static String TAG = "HttpAction";

    private static ApiInterface createApiInterface() {
        HashMap<String, String> baseHeader = new HashMap<>();
        baseHeader.put("baseHeader", "baseHeader value");
        HashMap<String, String> baseParam = new HashMap<>();
        baseParam.put("baseparam", "baseparam value");


        Retrofit retrofit = RetrofitFactory.getRetrofit(MyApplication.instance,ApiInterface.BASE_DOMAIN, baseHeader, baseParam);
        return retrofit.create(ApiInterface.class);
    }

    private static  void action(Observable<String> observable) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String responseBean) {
                        Log.e("xxx","responseBean");
                    }
                });
    }

    public static void getCityData() {
        action(createApiInterface().getCityData("dada"));
    }


    public interface ApiInterface {

        String BASE_DOMAIN = "https://www.lanxiniu.com";

        @POST("/Infos/getCityData")
        Observable<String> getCityData(@Query("cityId") String cityId);
    }
}
