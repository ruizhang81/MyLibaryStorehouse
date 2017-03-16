package client.bluerhino.cn.lib_image.upload.http;



import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zhangrui on 17/1/2.
 */

public class HttpAction {

    private final static String TAG = "HttpAction";


    public static ApiInterface getRetrofitUploadImage() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .build();
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(getUrl())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiInterface.class);
    }


    private static String getUrl() {
        String domain = ApiInterface.BASE_DOMAIN;
        String port = ApiInterface.BASE_PORT;
        if ("80".equals(port)) {
            port = "";
        } else {
            port = ":" + port;
        }
        return domain + port;
    }
}
