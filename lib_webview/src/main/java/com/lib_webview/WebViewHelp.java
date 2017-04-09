package com.lib_webview;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


/**
 * Created by zhangrui on 17/3/4.
 */

public class WebViewHelp {

    private WebView mWebView;
    private LinearLayout frame;
    private ProgressBar mProgressBar;

    public interface WebViewHelpCallback{
        void onCallback(String title);
    }

    public WebViewHelp(Activity activity, String loadUrl){
        this(activity,loadUrl,null);
    }

    public WebViewHelp(Activity activity, String loadUrl, final WebViewHelpCallback callback){
        mWebView = new WebView(activity.getApplicationContext());
        frame = (LinearLayout) activity.findViewById(R.id.frame);
        mProgressBar = (ProgressBar) activity.findViewById(R.id.h5_progress);
        frame.addView(mWebView);



        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(callback!=null){
                    callback.onCallback(title);
                }
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
        //如果不设置WebViewClient，请求会跳转系统浏览器
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

//                if (url.toString().contains("sina.cn")){
//                    view.loadUrl("http://ask.csdn.net/questions/178242");
//                    return true;
//                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (request.getUrl().toString().contains("sina.cn")){
//                        view.loadUrl("http://ask.csdn.net/questions/178242");
//                        return true;
//                    }
                }

                return false;
            }

        });
        mWebView.loadUrl(loadUrl);
    }

    public void onDestroy() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }


}
