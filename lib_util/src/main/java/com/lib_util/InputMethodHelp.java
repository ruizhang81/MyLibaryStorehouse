package com.lib_util;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by zhangrui on 17/3/7.
 */

public class InputMethodHelp {

    public void show(EditText edittext,Context context){
        edittext.requestFocus();
        edittext.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(edittext.getWindowToken(),0);
    }

    public void hide(EditText edittext,Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edittext.getWindowToken() , 0);
    }

    public void dialogShow(Dialog dialog){
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

}
