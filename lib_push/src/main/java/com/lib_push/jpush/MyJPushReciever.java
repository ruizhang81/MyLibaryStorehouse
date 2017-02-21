package com.lib_push.jpush;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lib_push.PushMain;
import com.lib_push.bean.PushMessage;

import cn.jpush.android.api.JPushInterface;

public class MyJPushReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        PushMessage pushMessage = new PushMessage();
        pushMessage.extra = extra;
        pushMessage.title = title;
        pushMessage.content = content;
        pushMessage.message = message;
        Log.e("xxx","intent.getAction() "+intent.getAction());

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            pushMessage.action = PushMessage.MESSAGE_RECEIVED;
            PushMain.dispatchPushMessage(pushMessage);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            pushMessage.action = PushMessage.NOTIFICATION_RECEIVED;
//            PushMain.dispatchPushMessage(pushMessage);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            pushMessage.action = PushMessage.NOTIFICATION_OPENED;
            PushMain.dispatchPushMessage(pushMessage);
        } else {

        }

    }



}


