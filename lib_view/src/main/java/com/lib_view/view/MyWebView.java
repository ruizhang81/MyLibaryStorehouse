package com.lib_view.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lib_view.R;

/**
 * Created by zhangrui on 17/1/5.
 */

public class MyWebView extends LinearLayout {

    private WebView mWebView;
    private FrameLayout frame;
    private ProgressBar mProgressBar;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_webview, this);
        mWebView = new WebView(context);
        frame = (FrameLayout) findViewById(R.id.frame);
        mProgressBar = (ProgressBar) findViewById(R.id.h5_progress);
        frame.addView(mWebView);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    mProgressBar.setProgress(100);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
            }


            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
            }

        });
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

}
