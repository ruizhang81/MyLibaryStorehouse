package com.crash;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;


/**
 * Created by zhangrui on 16/12/30.
 */

public class CrashAndUpdateUtil {

    private final static String APPID = "09a0e049e5";

    public static void init(Context context, boolean isDebug) {
        Bugly.init(context, APPID, isDebug);
        checkUpdate(false);
    }

    public static void checkUpdate(boolean isManual) {
        Beta.checkUpgrade(true, false);
    }


}
