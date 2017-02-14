package com.mylibarystorehouse; /**
 * Created by zhangrui on 17/2/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureHelp;
import com.google.zxing.client.android.ViewfinderView;
import com.mylibarystorehouse.R;

import client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui.CameraUtil;

/**
 * Created by zhangrui on 17/1/2.
 */

public class MainActivity extends Activity {

    private ViewfinderView viewfinderView;
    private SurfaceView preview_view;
    private CaptureHelp help;
    private TextView textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewfinderView = (ViewfinderView)findViewById(R.id.viewfinder_view);
        preview_view = (SurfaceView)findViewById(R.id.preview_view);
        textview = (TextView)findViewById(R.id.textview);
        help = new CaptureHelp(new CaptureHelp.OnSacnCallback() {
            @Override
            public void onCallback(String result) {
                textview.setText(result);
            }
        });
        help.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        help.onResume(this,viewfinderView,preview_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        help.onPause(preview_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        help.onDestroy();
    }



    private static final int SCAN_REQUEST_CODE = 1000;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CameraUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)) {

        }
    }

}
