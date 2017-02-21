package com.lib_push;


import android.content.Context;

import com.lib_push.bean.PushMessage;
import com.lib_push.jpush.JPushHelp;

public class PushMain {


    public static void resumePush(Context context){
        JPushHelp.getInstance(context).resumePush(context);
    }

    public static void stopPush(Context context){
        JPushHelp.getInstance(context).stopPush(context);
    }

    public static void registerAlias(Context context,String alias){
        JPushHelp.getInstance(context).registerJPushWith(context,alias);
    }

    public static void unRegisterAlias(Context context){
        JPushHelp.getInstance(context).unRegisterJPush(context);
    }

    public static void registerOnGetPushMessageListener(OnGetPushMessageListener listener){
        LISTENER = listener;
    }

    public static void dispatchPushMessage(PushMessage pushMessage){
        if(LISTENER!=null){
            LISTENER.onGetPushMessage(pushMessage);
        }
    }

    public static OnGetPushMessageListener LISTENER;

    public interface OnGetPushMessageListener{
         void onGetPushMessage(PushMessage pushMessage);
    }
}
