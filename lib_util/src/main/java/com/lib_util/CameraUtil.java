package com.lib_util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by zhangrui on 2/22/16.
 */
public class CameraUtil {

    private static final int REQUEST_PERMISSION_CAMERA_CODE = 1;

    public static boolean check(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager pm = activity.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(Manifest.permission.CAMERA, "packageName"));
            if (permission) {
                return true;
            }else {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA_CODE);
                return false;
            }
        } else {
            return true;
        }

    }



    public static boolean onRequestPermissionsResult(Activity activity,int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            if(grantResults.length>0){
                Log.e("xxx","grantResults[0]="+grantResults[0]);
                boolean result = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
//                save(activity);
                return true;
            }
        }
        return false;
    }
}
