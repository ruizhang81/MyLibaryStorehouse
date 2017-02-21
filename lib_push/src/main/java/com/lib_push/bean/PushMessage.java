package com.lib_push.bean;

/**
 * Created by zhangrui on 16/7/22.
 */
public class PushMessage {


    public final static int MESSAGE_RECEIVED = 0;
    public final static int NOTIFICATION_RECEIVED = 1;
    public final static int NOTIFICATION_OPENED = 2;


    public int action;
    public String title;
    public String content;
    public String extra;
    public String message;


    @Override
    public String toString() {
        return "PushMessage{" +
                "action=" + action +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", extra='" + extra + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
