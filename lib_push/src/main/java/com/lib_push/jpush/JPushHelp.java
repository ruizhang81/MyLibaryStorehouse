package com.lib_push.jpush;

import android.content.Context;
import android.text.TextUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * JPush
 *
 * @author chowjee
 * @date 2015-7-21
 */
public class JPushHelp {

    private static final int SET_TIME_OUT = 6002;
    private static JPushHelp instance;

    private JPushHelp(Context context) {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(context.getApplicationContext());
    }

    public static JPushHelp getInstance(Context context) {
        if(instance == null ){
            instance = new JPushHelp(context.getApplicationContext());
        }
        return instance;
    }

    private void registerJPushAlias(final Context appContext,String alias) {
        final String userAlias = alias;
        JPushInterface.setAlias(appContext.getApplicationContext(), userAlias, new TagAliasCallback() {
            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                if (arg0 == SET_TIME_OUT && appContext!=null ) {
                    JPushInterface.setAlias(appContext.getApplicationContext(), userAlias, this);
                }
            }
        });
    }

    public void registerJPushWith(Context context,String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        registerJPushAlias(context,alias);
    }

    public void unRegisterJPush(Context context) {
        registerJPushAlias(context,"");
    }


    public static void stopPush(Context context){
        JPushInterface.stopPush(context.getApplicationContext());
    }

    public static void resumePush(Context context){
        JPushInterface.resumePush(context.getApplicationContext());
    }
}
