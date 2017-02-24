package com.lib_util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by zhangrui on 2/22/16.
 */
public class CameraUtil {

    private static final int REQUEST_PERMISSION_CAMERA_CODE = 1;

    public static boolean check(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                requestCameraPermission(activity);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestCameraPermission(Activity activity) {
        activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA_CODE);
    }


    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            int grantResult = grantResults[0];
            return grantResult == PackageManager.PERMISSION_GRANTED;
        } else {
            return false;
        }
    }
}
