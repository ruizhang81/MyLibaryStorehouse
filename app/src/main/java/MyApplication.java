import android.app.Application;

import client.bluerhino.cn.lib_image.imageutil.ImageLoad;

/**
 * Created by zhangrui on 17/1/7.
 */

public class MyApplication extends Application {

    public static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        httptest.run();
//        ImageLoad.loadImage();
    }
}
