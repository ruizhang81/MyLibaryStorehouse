package com.lib_view.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib_view.R;


/**
 * Created by neo on 2016/1/4.
 * 请求网络的对话框工具类
 */
public class WaitDialog {
    private static Dialog mLoadingDialog;

    public static void showDialog(Activity mContext) {
        showDialog(mContext, "努力加载中", true);
    }

    public static void showDialog(Activity mContext, boolean cancelable) {
        showDialog(mContext, "努力加载中", cancelable);
    }

    public static void showDialog(Activity mContext, String text, boolean cancelable) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.loading_dialog, null);
        LinearLayout mRollLayout = (LinearLayout) mView.findViewById(R.id.loading_dialog_root);// 加载布局
        TextView tv = (TextView) mView.findViewById(R.id.loading_dialog_desc);
        tv.setText(text);
        final ImageView mRollImageView = (ImageView) mView.findViewById(R.id.loading_dialog_img);
        final Animation mRollAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_dialog_anim);

        mLoadingDialog = new Dialog(mContext, R.style.loading_dialog);// 创建自定义样式dialog
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setContentView(mRollLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        if (!mContext.isFinishing() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRollImageView.startAnimation(mRollAnimation);
                    mLoadingDialog.show();
                }
            });
        }
    }


    public static void dismissDialog(Activity mContext) {
        if (mContext != null && !mContext.isFinishing() && mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.dismiss();
                    mLoadingDialog = null;
                }
            });
        }
    }
}
