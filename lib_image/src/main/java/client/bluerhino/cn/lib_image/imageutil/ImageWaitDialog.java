package client.bluerhino.cn.lib_image.imageutil;

import android.app.ProgressDialog;
import android.content.Context;

import client.bluerhino.cn.lib_image.R;

/**
 * 等待dialog封装
 **/
public class ImageWaitDialog extends ProgressDialog {
    private static ImageWaitDialog mDialog;
    private static boolean isShowing;

    public ImageWaitDialog(Context context) {
        super(context);
    }

    public static ImageWaitDialog build(Context context) {
        return build(context, R.string.dialog_wait);
    }

    public static ImageWaitDialog build(Context context, int res) {
        if (!isShowing) {
            mDialog = new ImageWaitDialog(context);
            mDialog.setProgressStyle(R.style.CustomProgressDialog);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(context.getString(res));
        }
        return mDialog;
    }

    public static void update(String message) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.setMessage(message);
        }
    }

    public static void dis() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void cancel() {
        isShowing = false;
        super.cancel();
    }

    @Override
    public void dismiss() {
        isShowing = false;
        //采用try{}catch的方法临时解决显示加载对话框时，如果手机屏幕方向切换
        //此时会报View not attached to window manager的异常，导致程序崩溃
        //原因是由于调用dialog的dismiss方法时，启动dialog的activity不存在了
        //，activity由于屏幕转向而被销毁和重建了
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressDialog show(OnCancelListener listener) {
        try {
            if (!isShowing) {
                isShowing = true;
                this.setCancelable(true);
                this.setOnCancelListener(listener);
                this.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}