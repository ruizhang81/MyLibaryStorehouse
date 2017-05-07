package com.lib_view.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lib_view.R;


public class BannerViewIndicator extends RadioGroup implements
        OnPageChangeListener {
    private Context context;

    public BannerViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public interface MyOnPagerChangerListener {
        public void onPageSelected(int index);
    }

    private MyOnPagerChangerListener changerListener;

    public MyOnPagerChangerListener getChangerListener() {
        return changerListener;
    }

    public void setChangerListener(MyOnPagerChangerListener changerListener) {
        this.changerListener = changerListener;
    }

    private ViewPager viewPager;
    private int count;
    private int lastPosition = 0;

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, R.drawable.guide_indicator_selector);
    }

    public void setViewPager(ViewPager viewPager, int pointerSelectorRes) {
        this.viewPager = viewPager;
        count = viewPager.getAdapter().getCount();
        removeAllViews();
        for (int i = 0; i < count && count != 1; i++) {
            RadioButton radioButton = new RadioButton(context);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioButton.setButtonDrawable(pointerSelectorRes);
            int radius = (int) dp2Px(context, 9);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = radius;
            radioButton.setEnabled(false);
            radioButton.setLayoutParams(params);
            addView(radioButton);
        }
        viewPager.setOnPageChangeListener(this);
    }

    public static float dp2Px(Context context, float value) {
        if (context == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return typedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        RadioButton radioButton2 = (RadioButton) getChildAt(lastPosition);
        radioButton2.setChecked(false);
        RadioButton radioButton1 = (RadioButton) getChildAt(arg0);
        radioButton1.setChecked(true);
        lastPosition = arg0;
        if (changerListener != null) {
            changerListener.onPageSelected(arg0);
        }
    }

}
