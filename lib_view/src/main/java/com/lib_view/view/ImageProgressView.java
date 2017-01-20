package com.lib_view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by zhangrui on 3/10/16.
 */
public class ImageProgressView extends ImageView {

    public ImageProgressView.UploadImageStatus uploadImageStatus = new UploadImageStatus();
    private Paint mPaint;

    public ImageProgressView(Context context) {
        super(context);
        init();
    }

    public ImageProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (uploadImageStatus != null) {
            mPaint.setAntiAlias(true); // 消除锯齿
            mPaint.setStyle(Paint.Style.FILL);
            switch (uploadImageStatus.status) {
                case UploadImageStatus.image_normal:
                    display(canvas, 100, null, Color.parseColor("#00000000"));
                    break;
                case UploadImageStatus.upload_fail:
                    displayImage(canvas, Color.parseColor("#70000000"), false);
                    break;
                case UploadImageStatus.upload_ready:
                    display(canvas, 0, "准备上传", Color.parseColor("#70000000"));
                    break;
                case UploadImageStatus.upload_success:
                    displayImage(canvas, Color.parseColor("#70000000"), true);
                    break;
                case UploadImageStatus.upload_working:
                    display(canvas, uploadImageStatus.progress, uploadImageStatus.progress + "%", Color.parseColor("#70000000"));
                    break;
            }
        }
    }

    private void display(Canvas canvas, int progress, String displayStr, int bgColor) {
        mPaint.setColor(bgColor);
        canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress / 100, mPaint);
        if (displayStr != null) {
            mPaint.setTextSize(30);
            mPaint.setColor(Color.parseColor("#FFFFFF"));
            mPaint.setStrokeWidth(2);
            Rect rect = new Rect();
            mPaint.getTextBounds(displayStr, 0, displayStr.length(), rect);//确定文字的宽度
            canvas.drawText(displayStr, getWidth() / 2 - rect.width() / 2, getHeight() / 2, mPaint);
        }
    }

    private void displayImage(Canvas canvas, int bgColor, boolean success) {
        mPaint.setColor(bgColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        mPaint.setTextSize(30);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(2);
        Rect rect = new Rect();
        String displayStr = "点击重新上传";
        if (success) {
            displayStr = "上传成功";
        }
        mPaint.getTextBounds(displayStr, 0, displayStr.length(), rect);//确定文字的宽度
        canvas.drawText(displayStr, getWidth() / 2 - rect.width() / 2, getHeight() / 2, mPaint);
    }

    public void update() {
        postInvalidate();
    }


    public class UploadImageStatus {

        public final static int image_normal = 0;
        public final static int upload_ready = 1;
        public final static int upload_working = 2;
        public final static int upload_success = 3;
        public final static int upload_fail = 4;

        public String servicePath;
        public int progress;
        public int status;

        public UploadImageStatus() {
            this.status = image_normal;//初始化的时候是普通显示
        }

        public UploadImageStatus(String servicePath, int progress) {
            this.servicePath = servicePath;
            this.progress = 100;
            this.status = upload_success;
        }

    }
}
