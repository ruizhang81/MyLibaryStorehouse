package com.lib_util;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by zhangrui on 17/1/5.
 */

public class Phone {

    public static void call(Context context, String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            context.startActivity(intent);
        } catch (Exception e) {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(phoneNumber.trim());
            Toast.makeText(context,"电话号码已经复制,请在拨号界面粘贴",Toast.LENGTH_SHORT).show();
        }

    }
}
