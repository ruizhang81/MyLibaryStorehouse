package com.lib_view.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lib_view.R;


public abstract class GetCodeEditText extends RelativeLayout {

    private static final String TAG = "GetCodeEditText";
    private static final int GET_CODE_MAX_TIME = 30;
    private ClearEditText codeEdit;
    private ClearEditText phoneEdit;
    private Button mBtnGetCode;
    private int mCurrentCountDownTime = GET_CODE_MAX_TIME;
    /**
     * 倒计时器
     */
    CountDownTimer mCountDownTimer = new CountDownTimer((GET_CODE_MAX_TIME + 1) * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mBtnGetCode.setText(mCurrentCountDownTime + "秒");
            mBtnGetCode.setTextColor(ContextCompat.getColor(getContext(), R.color.text_red));
            mCurrentCountDownTime--;
        }

        @Override
        public void onFinish() {
            mBtnGetCode.setText("获取验证码");
            mBtnGetCode.setEnabled(true);
            mCurrentCountDownTime = GET_CODE_MAX_TIME;
        }
    };
    private String flag;
    /**
     * 设置输入字符串监听
     */
    private ClearEditText.OnTextListener onTextListener;

    public GetCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.layout_getcode_edit, this);
//        addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        codeEdit = (ClearEditText) findViewById(R.id.code_edit);
        mBtnGetCode = (Button) findViewById(R.id.btn_getCode);
    }

    public void setPhoneEdit(ClearEditText phoneEditP, final String flag) {
        this.phoneEdit = phoneEditP;
        this.flag = flag;
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().replaceAll(" ", "").length() >= 11) {
                    mBtnGetCode.setEnabled(true);
                    mBtnGetCode.setText("获取验证码");
                    mCurrentCountDownTime = GET_CODE_MAX_TIME;
                    mCountDownTimer.cancel();
                } else {
                    mBtnGetCode.setEnabled(false);
                }


                if (onTextListener != null) {
                    if (s == null) {
                        onTextListener.onChange(0);
                    } else {
                        onTextListener.onChange(s.length());
                    }
                }
            }
        });
        mBtnGetCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查手机号合法
                String phoneNumToGetCode = phoneEdit.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(phoneNumToGetCode) || phoneNumToGetCode.length() != 11 || !phoneNumToGetCode.startsWith("1")) {
                    MyToast.showToast(getContext(),"请输入正确的手机号码");
                    return;
                }
                mBtnGetCode.setEnabled(false);
                //请求动态验证码
                getSecurityCode(phoneNumToGetCode, flag, new OnHttpActionCallBack() {
                    @Override
                    public void onFinish() {
                        mBtnGetCode.setEnabled(true);
                    }

                    @Override
                    public void onSuccess() {
                        MyToast.showHeaderToast(getContext(),"验证码发送成功请注意查收");
                        mCountDownTimer.start();
                        mBtnGetCode.setEnabled(false);
                    }
                });
            }
        });
    }



    public String getText() {
        if (codeEdit != null) {
            return codeEdit.getText().toString().trim();
        }
        return "";
    }

    public void setOnTextListener(ClearEditText.OnTextListener onTextListener) {
        this.onTextListener = onTextListener;
    }

    public interface OnTextListener {
        void onChange(int length);
    }


    /** 网络请求**/
    public interface OnHttpActionCallBack {
        void onFinish();
        void onSuccess();
    }


    /**
     * 获取验证码
     *
     * @param phoneNum 合法的手机号
     *                 1短信验证码 2语音验证码
     */
    public abstract void getSecurityCode(String phoneNum,String flag,OnHttpActionCallBack callback);
}
