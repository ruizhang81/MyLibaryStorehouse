package com.lib_view.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.lib_view.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClearEditText extends EditText implements
        View.OnFocusChangeListener, TextWatcher {
    public final static int FORMAT_NOTHING = 0;
    public final static int FORMAT_PHONE = 1;
    public final static int FORMAT_IDCARD = 2;
    public final static int FORMAT_IDCARD_PHONE = 3;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;
    private StringBuilder preStrSB = new StringBuilder();
    /**
     * 设置输入字符串监听
     */
    private OnTextListener mOnTextListener;
    private int mFormatWay;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
//        	throw new NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.icon_edittext_delete);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);


        setTextColor(ContextCompat.getColor(getContext(), R.color.color_edit_text));
        setHintTextColor(ContextCompat.getColor(getContext(), R.color.color_edit_hint));
//        setTextSize(getResources().getDimension(R.dimen.edittext_text_size));
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mOnTextListener != null) {
            if (editable == null) {
                mOnTextListener.onChange(0);
            } else {
                mOnTextListener.onChange(editable.length());
            }
        }
        if (mFormatWay != FORMAT_NOTHING) {
            String strAfter = editable.toString();
            checkInput(strAfter);
        }
    }

    private void checkInput(String strAfter) {
//        Log.e("xxx", "checkInput textChangeBefore=(" + preStrSB.toString() + ") strAfter=(" + strAfter + ")");

        if (strAfter.equals(preStrSB.toString())) {
            return;
        }

        String realStr = strAfter.replaceAll(" ", "");
        int realLength = realStr.length();

        //前14位只能输入数字，如果输入的不是数字就还原
        if (realLength <= 14) {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(realStr);
            if (!m.matches()) {
                setText(preStrSB.toString());
                setSelection(preStrSB.toString().length());
                return;
            }
        }

        //检查长度是否合法
        if (mFormatWay == FORMAT_IDCARD_PHONE ||
                mFormatWay == FORMAT_IDCARD) {
            if (lengthIsIllegal(realLength, 18)) {
                return;
            }
        } else {
            if (lengthIsIllegal(realLength, 11)) {
                return;
            }
        }


        //初始化
        if (preStrSB.length() > 0) {
            preStrSB.delete(0, preStrSB.length());
        }
        preStrSB.append(realStr);


        int[] phoneFormat = {3, 7};
        int[] idcardFormat = {3, 6, 10, 12, 14};

        if (mFormatWay == FORMAT_IDCARD_PHONE) {
            if (realLength <= 11) {
                //如果11位及以下按电话号码format
                separateString(realLength, phoneFormat);
            } else {
                //如果11以上按身份证号码format
                separateString(realLength, idcardFormat);
            }
        } else if (mFormatWay == FORMAT_IDCARD) {
            separateString(realLength, idcardFormat);
        } else {
            separateString(realLength, phoneFormat);
        }
    }

    private boolean lengthIsIllegal(int realLength, int length) {
        if (realLength > length) {
            MyToast.showToast(getContext(), "最大长度为" + length + "个字符");
            setText(preStrSB.toString());
            setSelection(preStrSB.toString().length());
            return true;
        }
        return false;
    }

    private void separateString(int length, int... locations) {
        int addTime = 0;
        for (int i = 0; i < length; i++) {
            for (int location : locations) {
                if (location == i) {
                    preStrSB.insert(location + addTime, " ");
                    addTime++;
                }
            }
        }
        setText(preStrSB.toString());
        setSelection(preStrSB.toString().length());
    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public void setOnTextListener(OnTextListener onTextListener) {
        this.mOnTextListener = onTextListener;
    }

    public void setFormatWay(int formatWay) {
        this.mFormatWay = formatWay;
    }

    public String getTextString() {
        if (mFormatWay != FORMAT_NOTHING) {
            return getText().toString().replaceAll(" ", "");
        }
        return getText().toString();
    }

    public interface OnTextListener {
        void onChange(int length);
    }
}