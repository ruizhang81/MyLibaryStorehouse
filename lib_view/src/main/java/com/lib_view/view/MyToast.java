package com.lib_view.view;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.lib_view.R;


/**
 * Created by zhangrui on 17/1/4.
 */

public class MyToast {

    private static long mTime;

    public static void showToast(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            if (SystemClock.elapsedRealtime() - mTime > 2000) {
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                mTime = SystemClock.elapsedRealtime();
            }

        }
    }

    public static void showHeaderToast(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            if (SystemClock.elapsedRealtime() - mTime > 2000) {

                Toast toast = new Toast(context);
                TextView textView =
                        (TextView) LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
                textView.setText(content);
                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
                toast.setView(textView);
                toast.show();

                mTime = SystemClock.elapsedRealtime();

                //如果一定要在顶部，自定义view或者dialog
            }

        }
    }


}
